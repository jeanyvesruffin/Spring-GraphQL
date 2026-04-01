package ruffinjy.spring_graphql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ruffinjy.spring_graphql.entities.Achievement;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

}
