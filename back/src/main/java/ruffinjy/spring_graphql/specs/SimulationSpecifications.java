package ruffinjy.spring_graphql.specs;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import ruffinjy.spring_graphql.dtos.SimulationFilterInputDto;
import ruffinjy.spring_graphql.entities.Simulation;

import java.util.ArrayList;
import java.util.List;

public class SimulationSpecifications {

    public static Specification<Simulation> withFilter(SimulationFilterInputDto simulationFilter) {
        return (simulationRoot, simulationQuery, criteriaBuilder) -> {
            if (simulationFilter == null) return criteriaBuilder.conjunction();
            List<Predicate> predicates = new ArrayList<>();

            if (simulationFilter.getNameContains() != null && !simulationFilter.getNameContains().isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(simulationRoot.get("name")), "%" + simulationFilter.getNameContains().toLowerCase() + "%"));
            }
            if (simulationFilter.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(simulationRoot.get("status"), simulationFilter.getStatus()));
            }
            if (simulationFilter.getCreatedAfter() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(simulationRoot.get("createdAt"), simulationFilter.getCreatedAfter()));
            }
            if (simulationFilter.getMinProgress() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(simulationRoot.get("progress"), simulationFilter.getMinProgress()));
            }
            if (simulationFilter.getMaxProgress() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(simulationRoot.get("progress"), simulationFilter.getMaxProgress()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
