package ruffinjy.spring_graphql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ruffinjy.spring_graphql.config.GraphqlConfig;
import ruffinjy.spring_graphql.controllers.SimulationController;
import ruffinjy.spring_graphql.dtos.SimulationFilterInputDto;
import ruffinjy.spring_graphql.entities.Simulation;
import ruffinjy.spring_graphql.entities.enums.SimulationStatus;
import ruffinjy.spring_graphql.services.SimulationPublisherService;
import ruffinjy.spring_graphql.services.SimulationService;
import ruffinjy.spring_graphql.services.WidgetService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@GraphQlTest(controllers = SimulationController.class)
@Import(GraphqlConfig.class)
public class SimulationGraphQlTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private SimulationService simulationService;

    @MockitoBean
    private SimulationPublisherService simulationPublisherService;

    @MockitoBean
    private WidgetService widgetService;

    @Test
    void simulationsFiltered_returnsData() {
        Simulation s = new Simulation();
        s.setId(1L);
        s.setName("TestSim");
        s.setStatus(SimulationStatus.PENDING);
        s.setProgress(0.5);
        s.setCreatedAt(LocalDateTime.now());

        when(simulationService.findSimulationsByFilter(any(SimulationFilterInputDto.class), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(s), PageRequest.of(0, 10), 1));

        String query = "query($page: Int, $size: Int) { simulationsFiltered(page: $page, size: $size) { id name status progress } }";

        graphQlTester.document(query)
                .variable("page", 0)
                .variable("size", 10)
                .execute()
                .path("simulationsFiltered[0].name").entity(String.class).isEqualTo("TestSim");
    }
}
