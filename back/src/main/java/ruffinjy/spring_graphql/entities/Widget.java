package ruffinjy.spring_graphql.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ruffinjy.spring_graphql.entities.enums.WidgetType;

@Entity
@Table(name = "WIDGET")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Widget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "DASHBOARD_ID")
    private Dashboard dashboard;

    @Enumerated(EnumType.STRING)
    private WidgetType type;

    private String title;

    @Column(columnDefinition = "CLOB")
    private String config;

    @Column(name = "LINKED_SIMULATION_ID")
    private Long linkedSimulationId;

}
