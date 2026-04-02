package ruffinjy.spring_graphql.config;

import graphql.scalars.ExtendedScalars;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import graphql.execution.CoercedVariables;
import graphql.GraphQLContext;
import graphql.language.StringValue;
import graphql.language.Value;
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
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

@Configuration
public class GraphqlConfig {

    @Bean
    RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiring -> {
            GraphQLScalarType dateTimeScalar = GraphQLScalarType.newScalar()
                    .name("DateTime")
                    .description("DateTime scalar that accepts OffsetDateTime, ZonedDateTime, Instant and LocalDateTime")
                    .coercing(new Coercing<OffsetDateTime, String>() {
                        @Override
                        public String serialize(Object dataFetcherResult, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
                            OffsetDateTime odt = toOffsetDateTime(dataFetcherResult);
                            return odt == null ? null : odt.toString();
                        }

                        @Override
                        public OffsetDateTime parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
                            try {
                                return toOffsetDateTime(input);
                            } catch (CoercingSerializeException e) {
                                throw new CoercingParseValueException(e.getMessage(), e);
                            }
                        }

                        @Override
                        public OffsetDateTime parseLiteral(Value input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
                            if (input instanceof StringValue stringValue) {
                                String value = stringValue.getValue();
                                try {
                                    return OffsetDateTime.parse(value);
                                } catch (DateTimeParseException e) {
                                    try {
                                        LocalDateTime ldt = LocalDateTime.parse(value);
                                        return ldt.atZone(ZoneId.systemDefault()).toOffsetDateTime();
                                    } catch (DateTimeParseException ex) {
                                        throw new CoercingParseLiteralException("Invalid DateTime literal: " + value);
                                    }
                                }
                            }
                            throw new CoercingParseLiteralException("Expected AST type 'StringValue' for DateTime");
                        }

                        private OffsetDateTime toOffsetDateTime(Object input) {
                            if (input == null) return null;
                            if (input instanceof OffsetDateTime) return (OffsetDateTime) input;
                            if (input instanceof ZonedDateTime) return ((ZonedDateTime) input).toOffsetDateTime();
                            if (input instanceof Instant) return ((Instant) input).atOffset(ZoneOffset.UTC);
                            if (input instanceof LocalDateTime) return ((LocalDateTime) input).atZone(ZoneId.systemDefault()).toOffsetDateTime();
                            if (input instanceof String) {
                                String s = (String) input;
                                try {
                                    return OffsetDateTime.parse(s);
                                } catch (DateTimeParseException e) {
                                    try {
                                        LocalDateTime ldt = LocalDateTime.parse(s);
                                        return ldt.atZone(ZoneId.systemDefault()).toOffsetDateTime();
                                    } catch (DateTimeParseException ex) {
                                        throw new CoercingSerializeException("Invalid DateTime value: " + s);
                                    }
                                }
                            }
                            throw new CoercingSerializeException("Cannot convert value to OffsetDateTime: " + input.getClass().getName());
                        }
                    })
                    .build();

            wiring.scalar(dateTimeScalar).scalar(ExtendedScalars.Json);
        };
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
