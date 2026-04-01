package ruffinjy.spring_graphql.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PLAYER_STATISTIC")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MATCH_ID", nullable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "PLAYER_ID", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    private Integer minutesPlayed;

    private Integer goals;

    private Integer assists;

    private Integer yellowCards;

    private Integer redCards;

    private Integer shotsOnTarget;

    private LocalDateTime createdAt;

}
