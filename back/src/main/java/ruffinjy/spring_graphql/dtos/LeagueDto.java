package ruffinjy.spring_graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeagueDto {

    private Long id;

    private String name;

    private String code;

    private String country;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
