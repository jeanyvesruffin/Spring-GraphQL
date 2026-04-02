package ruffinjy.spring_graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ruffinjy.spring_graphql.entities.enums.WidgetType;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WidgetInputDto {
    private Long id;
    private WidgetType type;
    private String title;
    private Object config;
    private Long linkedSimulationId;
}
