package ruffinjy.spring_graphql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ruffinjy.spring_graphql.entities.Simulation;

public interface SimulationRepository extends JpaRepository<Simulation, Long>, JpaSpecificationExecutor<Simulation> {

}
