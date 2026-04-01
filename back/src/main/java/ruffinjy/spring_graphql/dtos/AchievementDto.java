package ruffinjy.spring_graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementDto {

    private Long id;

    private Long playerId;

    private Long teamId;

    private String title;

    private String description;

    private LocalDate achievedAt;

    private LocalDateTime createdAt;

}
