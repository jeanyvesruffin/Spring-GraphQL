package ruffinjy.spring_graphql.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ruffinjy.spring_graphql.entities.enums.SimulationStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SIMULATION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Simulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private SimulationStatus status;

    @Column(columnDefinition = "CLOB")
    private String config;

    private Double progress;

    private LocalDateTime createdAt;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SimulationResult> results = new ArrayList<>();

}
