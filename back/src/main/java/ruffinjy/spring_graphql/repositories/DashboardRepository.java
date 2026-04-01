package ruffinjy.spring_graphql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ruffinjy.spring_graphql.entities.Dashboard;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {

}
