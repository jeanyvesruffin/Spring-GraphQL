package ruffinjy.spring_graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationProgressDto {
    private String simulationId;
    private Double progress;
    private String message;
    private LocalDateTime timestamp;
}
