package ruffinjy.spring_graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationConfigInputDto {
    private Integer durationSeconds;
    private List<ParameterInputDto> parameters;
    private Integer seed;
    private String scenario;
}
