package ruffinjy.spring_graphql.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ruffinjy.spring_graphql.dtos.AchievementDto;
import ruffinjy.spring_graphql.entities.Achievement;

@Mapper(componentModel = "spring")
public interface AchievementMapper {

    @Mapping(target = "playerId", source = "player.id")
    @Mapping(target = "teamId", source = "team.id")
    AchievementDto toDto(Achievement achievement);

    @Mapping(target = "player.id", source = "playerId")
    @Mapping(target = "team.id", source = "teamId")
    Achievement toEntity(AchievementDto dto);

}
