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
@Table(name = "TEAM")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "LEAGUE_ID")
    private League league;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "SHORT_NAME", length = 50)
    private String shortName;

    private Integer foundedYear;

    @Column(length = 100)
    private String city;

    @Column(length = 200)
    private String stadium;

    private LocalDateTime createdAt;

}
