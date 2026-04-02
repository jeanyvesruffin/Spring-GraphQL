package ruffinjy.spring_graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDashboardInputDto {
    private Long id;
    private String name;
    private String description;
    private List<WidgetInputDto> widgets;
}
