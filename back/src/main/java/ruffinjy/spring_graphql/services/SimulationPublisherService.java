package ruffinjy.spring_graphql.services;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import ruffinjy.spring_graphql.dtos.SimulationProgressDto;
import ruffinjy.spring_graphql.entities.SimulationResult;
import ruffinjy.spring_graphql.dtos.MetricEvent;
import ruffinjy.spring_graphql.entities.Dashboard;

@Component
public class SimulationPublisherService {

    private final Sinks.Many<SimulationProgressDto> progressSink = Sinks.many().multicast().onBackpressureBuffer();
    private final Sinks.Many<SimulationResult> completedSink = Sinks.many().multicast().onBackpressureBuffer();
    private final Sinks.Many<MetricEvent> metricSink = Sinks.many().multicast().onBackpressureBuffer();
    private final Sinks.Many<Dashboard> dashboardSink = Sinks.many().multicast().onBackpressureBuffer();

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

    public void publishMetric(MetricEvent metricEvent) {
        metricSink.tryEmitNext(metricEvent);
    }

    public Flux<MetricEvent> metricFlux() {
        return metricSink.asFlux();
    }

    public void publishDashboardUpdate(Dashboard dashboard) {
        dashboardSink.tryEmitNext(dashboard);
    }

    public Flux<Dashboard> dashboardFlux() {
        return dashboardSink.asFlux();
    }
}
