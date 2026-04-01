package ruffinjy.spring_graphql.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ruffinjy.spring_graphql.dtos.PlayerStatisticDto;
import ruffinjy.spring_graphql.entities.PlayerStatistic;

@Mapper(componentModel = "spring")
public interface PlayerStatisticMapper {

    @Mapping(target = "matchId", source = "match.id")
    @Mapping(target = "playerId", source = "player.id")
    @Mapping(target = "teamId", source = "team.id")
    PlayerStatisticDto toDto(PlayerStatistic stat);

    @Mapping(target = "match.id", source = "matchId")
    @Mapping(target = "player.id", source = "playerId")
    @Mapping(target = "team.id", source = "teamId")
    PlayerStatistic toEntity(PlayerStatisticDto dto);

}
