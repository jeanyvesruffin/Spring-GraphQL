package ruffinjy.spring_graphql.config;

import graphql.scalars.ExtendedScalars;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.graphql.server.WebGraphQlInterceptor;
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
        return (graphqlRequest, graphqlRequestChain) -> {
            DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
            DataLoader<String, List<Widget>> widgetDataLoader = DataLoaderFactory.newMappedDataLoader((Set<String> dashboardIds) ->
                    CompletableFuture.supplyAsync(() -> widgetService.findByDashboardIds(dashboardIds)));
            dataLoaderRegistry.register("widgetsByDashboard", widgetDataLoader);

            graphqlRequest.configureExecutionInput((executionInput, builder) -> builder.dataLoaderRegistry(dataLoaderRegistry).build());
            return graphqlRequestChain.next(graphqlRequest);
        };
    }
}
