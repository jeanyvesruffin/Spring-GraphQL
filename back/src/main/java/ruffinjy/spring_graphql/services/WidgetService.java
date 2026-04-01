package ruffinjy.spring_graphql.services;

import org.springframework.stereotype.Service;
import ruffinjy.spring_graphql.repositories.WidgetRepository;
import ruffinjy.spring_graphql.entities.Widget;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WidgetService {

    private final WidgetRepository widgetRepository;

    public WidgetService(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    public Map<String, List<Widget>> findByDashboardIds(Set<String> dashboardKeySet) {
        if (dashboardKeySet == null || dashboardKeySet.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Long> ids = dashboardKeySet.stream().map(Long::valueOf).collect(Collectors.toList());
        List<Widget> widgets = widgetRepository.findByDashboardIdIn(ids);
        Map<Long, List<Widget>> grouped = widgets.stream()
                .collect(Collectors.groupingBy(currentWidget -> currentWidget.getDashboard().getId()));

        Map<String, List<Widget>> result = new HashMap<>();
        for (String dashboardKey : dashboardKeySet) {
            Long dashboardId = Long.valueOf(dashboardKey);
            result.put(dashboardKey, grouped.getOrDefault(dashboardId, Collections.emptyList()));
        }
        return result;
    }
}
