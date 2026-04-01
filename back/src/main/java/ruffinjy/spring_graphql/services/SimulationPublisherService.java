package ruffinjy.spring_graphql.services;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import ruffinjy.spring_graphql.dtos.SimulationProgressDto;
import ruffinjy.spring_graphql.entities.SimulationResult;

@Component
public class SimulationPublisherService {

    private final Sinks.Many<SimulationProgressDto> progressSink = Sinks.many().multicast().onBackpressureBuffer();
    private final Sinks.Many<SimulationResult> completedSink = Sinks.many().multicast().onBackpressureBuffer();

    public void publishProgress(SimulationProgressDto simulationProgressDto) {
        progressSink.tryEmitNext(simulationProgressDto);
    }

    public Flux<SimulationProgressDto> progressFlux() {
        return progressSink.asFlux();
    }

    public void publishCompleted(SimulationResult simulationResult) {
        completedSink.tryEmitNext(simulationResult);
    }

    public Flux<SimulationResult> completedFlux() {
        return completedSink.asFlux();
    }
}
