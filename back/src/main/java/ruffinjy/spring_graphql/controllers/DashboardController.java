package ruffinjy.spring_graphql.controllers;

import tools.jackson.databind.ObjectMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import ruffinjy.spring_graphql.dtos.UpdateDashboardInputDto;
import ruffinjy.spring_graphql.dtos.WidgetInputDto;
import ruffinjy.spring_graphql.entities.Dashboard;
import ruffinjy.spring_graphql.entities.Widget;
import ruffinjy.spring_graphql.repositories.DashboardRepository;
import ruffinjy.spring_graphql.repositories.WidgetRepository;
import ruffinjy.spring_graphql.services.SimulationPublisherService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class DashboardController {

    private final DashboardRepository dashboardRepository;
    private final WidgetRepository widgetRepository;
    private final SimulationPublisherService simulationPublisher;
    private final ObjectMapper objectMapper;

    public DashboardController(DashboardRepository dashboardRepository, WidgetRepository widgetRepository, SimulationPublisherService simulationPublisher, ObjectMapper objectMapper) {
        this.dashboardRepository = dashboardRepository;
        this.widgetRepository = widgetRepository;
        this.simulationPublisher = simulationPublisher;
        this.objectMapper = objectMapper;
    }

    @QueryMapping
    public List<Dashboard> dashboards() {
        return dashboardRepository.findAll();
    }

    @QueryMapping
    public Dashboard dashboard(@Argument Long id) {
        return dashboardRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Dashboard updateDashboard(@Argument UpdateDashboardInputDto input) {
        Dashboard dashboard = dashboardRepository.findById(input.getId()).orElseThrow(() -> new NoSuchElementException("Dashboard not found"));
        if (input.getName() != null) dashboard.setName(input.getName());
        if (input.getDescription() != null) dashboard.setDescription(input.getDescription());

        if (input.getWidgets() != null) {
            List<Widget> newWidgets = new ArrayList<>();
            for (WidgetInputDto w : input.getWidgets()) {
                Widget widget;
                if (w.getId() != null) {
                    widget = widgetRepository.findById(w.getId()).orElse(new Widget());
                } else {
                    widget = new Widget();
                }
                widget.setDashboard(dashboard);
                widget.setType(w.getType());
                widget.setTitle(w.getTitle());
                widget.setConfig(writeJson(w.getConfig()));
                widget.setLinkedSimulationId(w.getLinkedSimulationId());
                widget = widgetRepository.save(widget);
                newWidgets.add(widget);
            }
            dashboard.setWidgets(newWidgets);
        }

        Dashboard saved = dashboardRepository.save(dashboard);
        simulationPublisher.publishDashboardUpdate(saved);
        return saved;
    }

    @MutationMapping
    public Widget addWidget(@Argument Long dashboardId, @Argument WidgetInputDto input) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId).orElseThrow(() -> new NoSuchElementException("Dashboard not found"));
        Widget widget = new Widget();
        widget.setDashboard(dashboard);
        widget.setType(input.getType());
        widget.setTitle(input.getTitle());
        widget.setConfig(writeJson(input.getConfig()));
        widget.setLinkedSimulationId(input.getLinkedSimulationId());
        Widget saved = widgetRepository.save(widget);
        simulationPublisher.publishDashboardUpdate(dashboardRepository.findById(dashboardId).orElse(dashboard));
        return saved;
    }

    @MutationMapping
    public Boolean removeWidget(@Argument Long id) {
        try {
            widgetRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @SubscriptionMapping
    public Flux<Dashboard> dashboardUpdated(@Argument String dashboardId) {
        return simulationPublisher.dashboardFlux().filter(d -> d.getId().toString().equals(dashboardId));
    }

    @SchemaMapping(typeName = "Widget", field = "dashboardId")
    public String widgetDashboardId(Widget widget) {
        return widget.getDashboard() != null ? widget.getDashboard().getId().toString() : null;
    }

    @SchemaMapping(typeName = "Widget", field = "config")
    public Object widgetConfig(Widget widget) {
        if (widget.getConfig() == null) return null;
        try {
            return objectMapper.readValue(widget.getConfig(), Object.class);
        } catch (Exception e) {
            return null;
        }
    }

    private String writeJson(Object value) {
        if (value == null) return null;
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            return null;
        }
    }
}
