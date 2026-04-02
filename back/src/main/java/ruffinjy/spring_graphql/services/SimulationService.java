package ruffinjy.spring_graphql.services;

import tools.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ruffinjy.spring_graphql.dtos.CreateSimulationInputDto;
import ruffinjy.spring_graphql.dtos.MetricDto;
import ruffinjy.spring_graphql.dtos.SimulationConfigInputDto;
import ruffinjy.spring_graphql.dtos.SimulationFilterInputDto;
import ruffinjy.spring_graphql.dtos.SimulationProgressDto;
import ruffinjy.spring_graphql.dtos.SimulationSummaryDto;
import ruffinjy.spring_graphql.entities.Simulation;
import ruffinjy.spring_graphql.entities.SimulationResult;
import ruffinjy.spring_graphql.entities.enums.SimulationStatus;
import ruffinjy.spring_graphql.repositories.SimulationRepository;
import ruffinjy.spring_graphql.specs.SimulationSpecifications;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SimulationService {

    private final SimulationRepository simulationRepository;
    private final SimulationPublisherService simulationPublisher;
    private final ObjectMapper objectMapper;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Map<Long, Future<?>> runningSimulations = new ConcurrentHashMap<>();

    public SimulationService(SimulationRepository simulationRepository, SimulationPublisherService simulationPublisher, ObjectMapper objectMapper) {
        this.simulationRepository = simulationRepository;
        this.simulationPublisher = simulationPublisher;
        this.objectMapper = objectMapper;
    }

    public Optional<Simulation> findById(Long simulationId) {
        return simulationRepository.findById(simulationId);
    }

    public Page<Simulation> findSimulations(SimulationStatus simulationStatus, int offset, int limit) {
        int lim = (limit <= 0) ? 20 : limit;
        int page = lim == 0 ? 0 : (offset / lim);
        Pageable pageable = PageRequest.of(page, lim, Sort.by("createdAt").descending());
        if (simulationStatus == null) {
            return simulationRepository.findAll(pageable);
        }
        return simulationRepository.findAll((simulationRoot, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(simulationRoot.get("status"), simulationStatus), pageable);
    }

    public Page<Simulation> findSimulationsByFilter(SimulationFilterInputDto filter, int page, int size) {
        int maxPage = Math.max(0, page);
        int maxSize = Math.max(1, size);
        Pageable pageable = PageRequest.of(maxPage, maxSize, Sort.by("createdAt").descending());
        return simulationRepository.findAll(SimulationSpecifications.withFilter(filter), pageable);
    }

    public Simulation createSimulation(CreateSimulationInputDto createSimulationInputDto) {
        Simulation simulation = new Simulation();
        simulation.setName(createSimulationInputDto.getName());
        simulation.setStatus(SimulationStatus.PENDING);
        SimulationConfigInputDto inputSimulationConfig = createSimulationInputDto.getConfig();
        if (inputSimulationConfig != null) {
            if (inputSimulationConfig.getParameters() == null) {
                inputSimulationConfig.setParameters(Collections.emptyList());
            }
            try {
                simulation.setConfig(objectMapper.writeValueAsString(inputSimulationConfig));
            } catch (Exception e) {
                simulation.setConfig("{}");
            }
        }
        simulation.setProgress(0.0);
        simulation.setCreatedAt(LocalDateTime.now());
        return simulationRepository.save(simulation);
    }

    public Simulation startSimulation(Long simulationId) {
        Simulation simulation = simulationRepository.findById(simulationId).orElseThrow(() -> new NoSuchElementException("Simulation not found"));
        simulation.setStatus(SimulationStatus.RUNNING);
        simulation.setStartedAt(LocalDateTime.now());
        simulationRepository.save(simulation);

        Future<?> future = executor.submit(() -> {
            try {
                for (int iterationCount = 1; iterationCount <= 100; iterationCount += 10) {
                    Thread.sleep(500);

                    Simulation current = simulationRepository.findById(simulationId).orElse(null);
                    if (current == null || current.getStatus() == SimulationStatus.CANCELLED || current.getStatus() == SimulationStatus.FAILED) {
                        return;
                    }

                    current.setProgress(iterationCount / 100.0);
                    simulationRepository.save(current);
                    simulationPublisher.publishProgress(new SimulationProgressDto(String.valueOf(current.getId()), current.getProgress(), "progress " + iterationCount, LocalDateTime.now()));
                }

                Simulation current = simulationRepository.findById(simulationId).orElse(null);
                if (current == null || current.getStatus() == SimulationStatus.CANCELLED || current.getStatus() == SimulationStatus.FAILED) {
                    return;
                }

                current.setStatus(SimulationStatus.COMPLETED);
                current.setEndedAt(LocalDateTime.now());
                current.setProgress(1.0);
                simulationRepository.save(current);

                SimulationResult simulationResult = new SimulationResult();
                simulationResult.setSimulation(current);
                simulationResult.setMetric("summary");
                simulationResult.setValue(1.0);
                simulationResult.setTimestamp(LocalDateTime.now());
                simulationPublisher.publishCompleted(simulationResult);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                runningSimulations.remove(simulationId);
            }
        });

        runningSimulations.put(simulationId, future);
        return simulation;
    }

    public Simulation stopSimulation(Long simulationId) {
        Simulation simulation = simulationRepository.findById(simulationId).orElseThrow();
        simulation.setStatus(SimulationStatus.CANCELLED);
        simulation.setEndedAt(LocalDateTime.now());
        Simulation saved = simulationRepository.save(simulation);

        Future<?> future = runningSimulations.remove(simulationId);
        if (future != null) {
            future.cancel(true);
        }

        return saved;
    }

    public List<SimulationSummaryDto> findSimulationsSummary(int limit) {
        int lim = Math.max(1, limit <= 0 ? 10 : limit);
        Pageable pageable = PageRequest.of(0, lim, Sort.by("createdAt").descending());
        return simulationRepository.findAll(pageable).getContent().stream()
                .map(simulation -> new SimulationSummaryDto(simulation.getId().toString(), simulation.getName(), simulation.getStatus(), simulation.getProgress()))
                .collect(Collectors.toList());
    }

    public List<MetricDto> getMetrics(Long simulationId, LocalDateTime since) {
        return simulationRepository.findById(simulationId)
                .map(simulation -> simulation.getResults().stream()
                        .filter(simulationResult -> since == null || (simulationResult.getTimestamp() != null && simulationResult.getTimestamp().isAfter(since)))
                        .map(simulationResult -> new MetricDto(simulationResult.getMetric(), simulationResult.getValue(), simulationResult.getTimestamp()))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

}
