package ruffinjy.spring_graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {

    private Long id;

    private Long teamId;

    private String firstName;

    private String lastName;

    private LocalDate birthdate;

    private String position;

    private Integer number;

    private String nationality;

    private LocalDate contractUntil;

    private LocalDateTime createdAt;

}
