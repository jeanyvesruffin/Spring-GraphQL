package ruffinjy.spring_graphql.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ruffinjy.spring_graphql.dtos.CreateSimulationInputDto;
import ruffinjy.spring_graphql.dtos.SimulationConfigInputDto;
import ruffinjy.spring_graphql.dtos.SimulationFilterInputDto;
import ruffinjy.spring_graphql.dtos.SimulationProgressDto;
import ruffinjy.spring_graphql.entities.Simulation;
import ruffinjy.spring_graphql.entities.SimulationResult;
import ruffinjy.spring_graphql.entities.enums.SimulationStatus;
import ruffinjy.spring_graphql.repositories.SimulationRepository;
import ruffinjy.spring_graphql.specs.SimulationSpecifications;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SimulationService {

    private final SimulationRepository simulationRepository;
    private final SimulationPublisherService simulationPublisher;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public SimulationService(SimulationRepository simulationRepository, SimulationPublisherService simulationPublisher) {
        this.simulationRepository = simulationRepository;
        this.simulationPublisher = simulationPublisher;
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
            simulation.setConfig("{\"durationSeconds\":" + (inputSimulationConfig.getDurationSeconds() == null ? 0 : inputSimulationConfig.getDurationSeconds()) + "}");
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

        executor.submit(() -> {
            try {
                for (int iterationCount = 1; iterationCount <= 100; iterationCount += 10) {
                    Thread.sleep(500);
                    simulation.setProgress(iterationCount / 100.0);
                    simulationRepository.save(simulation);
                    simulationPublisher.publishProgress(new SimulationProgressDto(String.valueOf(simulation.getId()), simulation.getProgress(), "progress " + iterationCount, LocalDateTime.now()));
                }

                simulation.setStatus(SimulationStatus.COMPLETED);
                simulation.setEndedAt(LocalDateTime.now());
                simulation.setProgress(1.0);
                simulationRepository.save(simulation);

                SimulationResult simulationResult = new SimulationResult();
                simulationResult.setSimulation(simulation);
                simulationResult.setMetric("summary");
                simulationResult.setValue(1.0);
                simulationResult.setTimestamp(LocalDateTime.now());
                simulationPublisher.publishCompleted(simulationResult);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        return simulation;
    }

    public Simulation stopSimulation(Long simulationId) {
        Simulation simulation = simulationRepository.findById(simulationId).orElseThrow();
        simulation.setStatus(SimulationStatus.CANCELLED);
        simulation.setEndedAt(LocalDateTime.now());
        return simulationRepository.save(simulation);
    }

}
