package ruffinjy.spring_graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ruffinjy.spring_graphql.entities.enums.SimulationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationSummaryDto {
    private String id;
    private String name;
    private SimulationStatus status;
    private Double progress;
}
