package ruffinjy.spring_graphql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ruffinjy.spring_graphql.entities.Widget;

import java.util.List;

public interface WidgetRepository extends JpaRepository<Widget, Long> {
    List<Widget> findByDashboardIdIn(List<Long> dashboardIds);
}
