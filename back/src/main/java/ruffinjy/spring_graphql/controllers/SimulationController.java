package ruffinjy.spring_graphql.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import ruffinjy.spring_graphql.dtos.CreateSimulationInputDto;
import ruffinjy.spring_graphql.dtos.SimulationFilterInputDto;
import ruffinjy.spring_graphql.dtos.SimulationProgressDto;
import ruffinjy.spring_graphql.dtos.SimulationSummaryDto;
import ruffinjy.spring_graphql.entities.Simulation;
import ruffinjy.spring_graphql.entities.Widget;
import ruffinjy.spring_graphql.entities.enums.SimulationStatus;
import ruffinjy.spring_graphql.entities.SimulationResult;
import ruffinjy.spring_graphql.services.SimulationPublisherService;
import ruffinjy.spring_graphql.services.SimulationService;

import java.util.List;

@Controller
public class SimulationController {

    private final SimulationService simulationService;
    private final SimulationPublisherService simulationPublisher;

    public SimulationController(SimulationService simulationService, SimulationPublisherService simulationPublisher) {
        this.simulationService = simulationService;
        this.simulationPublisher = simulationPublisher;
    }

    @QueryMapping
    public List<Simulation> simulations(@Argument SimulationStatus status, @Argument Integer offset, @Argument Integer limit) {
        return simulationService.findSimulations(status, offset == null ? 0 : offset, limit == null ? 20 : limit).getContent();
    }

    @QueryMapping
    public java.util.List<Simulation> simulationsFiltered(@Argument SimulationFilterInputDto simulationFilter,
                                                         @Argument Integer page,
                                                         @Argument Integer size) {
        return simulationService.findSimulationsByFilter(simulationFilter, page == null ? 0 : page, size == null ? 20 : size).getContent();
    }

    @QueryMapping
    public Simulation simulation(@Argument Long simulationId) {
        return simulationService.findById(simulationId).orElse(null);
    }

    @MutationMapping
    public Simulation createSimulation(@Argument CreateSimulationInputDto createSimulationInputDto) {
        return simulationService.createSimulation(createSimulationInputDto);
    }

    @MutationMapping
    public Simulation startSimulation(@Argument Long simulationId) {
        return simulationService.startSimulation(simulationId);
    }

    @MutationMapping
    public Simulation stopSimulation(@Argument Long simulationId) {
        return simulationService.stopSimulation(simulationId);
    }

    @SubscriptionMapping
    public Flux<SimulationProgressDto> simulationProgress(@Argument String simulationId) {
        return simulationPublisher.progressFlux().filter(progressUpdate -> progressUpdate.getSimulationId().equals(simulationId));
    }

    @SubscriptionMapping
    public Flux<SimulationResult> simulationCompleted(@Argument String simulationId) {
        return simulationPublisher.completedFlux().filter(result -> result.getSimulation().getId().toString().equals(simulationId));
    }

    @SchemaMapping(typeName = "Widget", field = "linkedSimulation")
    public SimulationSummaryDto linkedSimulation(Widget widget) {
        if (widget.getLinkedSimulationId() == null) return null;
        return simulationService.findById(widget.getLinkedSimulationId())
                .map(simulation -> new SimulationSummaryDto(simulation.getId().toString(), simulation.getName(), simulation.getStatus(), simulation.getProgress()))
                .orElse(null);
    }

}
