package ruffinjy.spring_graphql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ruffinjy.spring_graphql.entities.League;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {

}
