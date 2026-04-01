package ruffinjy.spring_graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ruffinjy.spring_graphql.entities.enums.SimulationStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationFilterInputDto {
    private String nameContains;
    private SimulationStatus status;
    private LocalDateTime createdAfter;
    private Double minProgress;
    private Double maxProgress;
}
