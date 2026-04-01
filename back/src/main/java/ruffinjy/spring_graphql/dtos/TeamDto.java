package ruffinjy.spring_graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {

    private Long id;

    private Long leagueId;

    private String name;

    private String shortName;

    private Integer foundedYear;

    private String city;

    private String stadium;

    private LocalDateTime createdAt;

}
