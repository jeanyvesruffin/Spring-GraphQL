package ruffinjy.spring_graphql.entities;

import jakarta.persistence.Column;
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
@Table(name = "MATCHES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "LEAGUE_ID")
    private League league;

    @Column(length = 20)
    private String season;

    @Column(name = "MATCH_DATE", nullable = false)
    private LocalDateTime matchDate;

    @ManyToOne
    @JoinColumn(name = "HOME_TEAM_ID", nullable = false)
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "AWAY_TEAM_ID", nullable = false)
    private Team awayTeam;

    private Integer homeScore;

    private Integer awayScore;

    @Column(length = 50)
    private String status;

    @Column(length = 200)
    private String venue;

    private LocalDateTime createdAt;

}
