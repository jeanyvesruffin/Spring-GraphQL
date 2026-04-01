package ruffinjy.spring_graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto {

    private Long id;

    private Long leagueId;

    private String season;

    private LocalDateTime matchDate;

    private Long homeTeamId;

    private Long awayTeamId;

    private Integer homeScore;

    private Integer awayScore;

    private String status;

    private String venue;

    private LocalDateTime createdAt;

}
