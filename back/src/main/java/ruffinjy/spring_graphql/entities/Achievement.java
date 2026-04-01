package ruffinjy.spring_graphql.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ACHIEVEMENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PLAYER_ID")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column
    private String description;

    private LocalDate achievedAt;

    private LocalDateTime createdAt;

}
