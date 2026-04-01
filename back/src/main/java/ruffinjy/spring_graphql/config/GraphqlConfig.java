package ruffinjy.spring_graphql.config;

import graphql.scalars.ExtendedScalars;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import reactor.core.publisher.Mono;
import ruffinjy.spring_graphql.entities.Widget;
import ruffinjy.spring_graphql.services.WidgetService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Configuration
public class GraphqlConfig {

    @Bean
    RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiring -> wiring
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.Json);
    }

    @Bean
    WebGraphQlInterceptor dataLoaderInterceptor(WidgetService widgetService) {
        return (request, chain) -> {
            DataLoaderRegistry registry = new DataLoaderRegistry();
            DataLoader<String, List<Widget>> loader = DataLoader.newMappedDataLoader((Set<String> keys) ->
                    CompletableFuture.supplyAsync(() -> widgetService.findByDashboardIds(keys)));
            registry.register("widgetsByDashboard", loader);

            request.configureExecutionInput((executionInput, builder) -> builder.dataLoaderRegistry(registry).build());
            return chain.next(request);
        };
    }
}
