package ruffinjy.spring_graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStatisticDto {

    private Long id;

    private Long matchId;

    private Long playerId;

    private Long teamId;

    private Integer minutesPlayed;

    private Integer goals;

    private Integer assists;

    private Integer yellowCards;

    private Integer redCards;

    private Integer shotsOnTarget;

    private LocalDateTime createdAt;

}
