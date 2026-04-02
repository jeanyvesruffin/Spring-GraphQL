package ruffinjy.spring_graphql.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import ruffinjy.spring_graphql.dtos.CreateSimulationInputDto;
import ruffinjy.spring_graphql.dtos.SimulationConfigInputDto;
import ruffinjy.spring_graphql.dtos.SimulationFilterInputDto;
import ruffinjy.spring_graphql.dtos.SimulationProgressDto;
import ruffinjy.spring_graphql.dtos.SimulationSummaryDto;
import ruffinjy.spring_graphql.dtos.MetricDto;
import ruffinjy.spring_graphql.dtos.MetricInputDto;
import ruffinjy.spring_graphql.dtos.MetricEvent;
import ruffinjy.spring_graphql.entities.Simulation;
import ruffinjy.spring_graphql.entities.Widget;
import ruffinjy.spring_graphql.entities.enums.SimulationStatus;
import ruffinjy.spring_graphql.entities.SimulationResult;
import ruffinjy.spring_graphql.services.SimulationPublisherService;
import ruffinjy.spring_graphql.services.SimulationService;

import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SimulationController {

    private final SimulationService simulationService;
    private final SimulationPublisherService simulationPublisher;
    private final ObjectMapper objectMapper;

    public SimulationController(SimulationService simulationService, SimulationPublisherService simulationPublisher, ObjectMapper objectMapper) {
        this.simulationService = simulationService;
        this.simulationPublisher = simulationPublisher;
        this.objectMapper = objectMapper;
    }

    @QueryMapping
    public List<Simulation> simulations(@Argument SimulationStatus status, @Argument Integer offset, @Argument Integer limit) {
        return simulationService.findSimulations(status, offset == null ? 0 : offset, limit == null ? 20 : limit).getContent();
    }

    @QueryMapping
    public java.util.List<Simulation> simulationsFiltered(@Argument("filter") SimulationFilterInputDto filter,
                                                         @Argument Integer page,
                                                         @Argument Integer size) {
        return simulationService.findSimulationsByFilter(filter, page == null ? 0 : page, size == null ? 20 : size).getContent();
    }

    @QueryMapping
    public Simulation simulation(@Argument("id") Long simulationId) {
        return simulationService.findById(simulationId).orElse(null);
    }

    @MutationMapping
    public Simulation createSimulation(@Argument("input") CreateSimulationInputDto input) {
        return simulationService.createSimulation(input);
    }

    @MutationMapping
    public Simulation startSimulation(@Argument("id") Long simulationId) {
        return simulationService.startSimulation(simulationId);
    }

    @MutationMapping
    public Simulation stopSimulation(@Argument("id") Long simulationId) {
        return simulationService.stopSimulation(simulationId);
    }

    @MutationMapping
    public Simulation cancelSimulation(@Argument("id") Long id) {
        return simulationService.stopSimulation(id);
    }

    @QueryMapping
    public java.util.List<SimulationSummaryDto> simulationsSummary(@Argument("limit") Integer limit) {
        return simulationService.findSimulationsSummary(limit == null ? 10 : limit);
    }

    @QueryMapping
    public java.util.List<MetricDto> metrics(@Argument("simulationId") Long simulationId, @Argument OffsetDateTime since) {
        LocalDateTime sinceLocal = since != null ? since.toLocalDateTime() : null;
        return simulationService.getMetrics(simulationId, sinceLocal);
    }

    @SubscriptionMapping
    public Flux<SimulationProgressDto> simulationProgress(@Argument("simulationId") String simulationId) {
        return simulationPublisher.progressFlux().filter(progressUpdate -> progressUpdate.getSimulationId().equals(simulationId));
    }

    @SubscriptionMapping
    public Flux<SimulationResult> simulationCompleted(@Argument("simulationId") String simulationId) {
        return simulationPublisher.completedFlux().filter(result -> result.getSimulation().getId().toString().equals(simulationId));
    }

    @MutationMapping
    public MetricDto publishMetric(@Argument("simulationId") Long simulationId, @Argument MetricInputDto metric) {
        LocalDateTime ts = metric.getTimestamp() != null ? metric.getTimestamp() : LocalDateTime.now();
        MetricDto dto = new MetricDto(metric.getName(), metric.getValue(), ts);
        simulationPublisher.publishMetric(new MetricEvent(String.valueOf(simulationId), dto));
        return dto;
    }

    @SubscriptionMapping
    public Flux<MetricDto> metricUpdated(@Argument("simulationId") String simulationId) {
        return simulationPublisher.metricFlux().filter(ev -> ev.getSimulationId().equals(simulationId)).map(MetricEvent::getMetric);
    }

    @SchemaMapping(typeName = "Widget", field = "linkedSimulation")
    public SimulationSummaryDto linkedSimulation(Widget widget) {
        if (widget.getLinkedSimulationId() == null) return null;
        return simulationService.findById(widget.getLinkedSimulationId())
                .map(simulation -> new SimulationSummaryDto(simulation.getId().toString(), simulation.getName(), simulation.getStatus(), simulation.getProgress()))
                .orElse(null);
    }

    @SchemaMapping(typeName = "Simulation", field = "config")
    public SimulationConfigInputDto simulationConfig(Simulation simulation) {
        if (simulation.getConfig() == null) {
            return new SimulationConfigInputDto(0, Collections.emptyList(), null, null);
        }
        try {
            SimulationConfigInputDto config = objectMapper.readValue(simulation.getConfig(), SimulationConfigInputDto.class);
            if (config.getDurationSeconds() == null) config.setDurationSeconds(0);
            if (config.getParameters() == null) config.setParameters(Collections.emptyList());
            return config;
        } catch (Exception e) {
            return new SimulationConfigInputDto(0, Collections.emptyList(), null, null);
        }
    }

    @SchemaMapping(typeName = "Simulation", field = "metrics")
    public List<MetricDto> simulationMetrics(Simulation simulation) {
        if (simulation.getResults() == null) return List.of();
        return simulation.getResults().stream()
                .map(r -> new MetricDto(r.getMetric(), r.getValue(), r.getTimestamp()))
                .collect(Collectors.toList());
    }

}
