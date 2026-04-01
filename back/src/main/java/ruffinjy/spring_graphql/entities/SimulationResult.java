package ruffinjy.spring_graphql.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "SIMULATION_RESULT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SIMULATION_ID")
    private Simulation simulation;

    @Column(nullable = false)
    private String metric;

    private Double value;

    private LocalDateTime timestamp;

}
