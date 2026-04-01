package ruffinjy.spring_graphql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ruffinjy.spring_graphql.entities.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

}
