package ruffinjy.spring_graphql.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ruffinjy.spring_graphql.dtos.TeamDto;
import ruffinjy.spring_graphql.entities.Team;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(target = "leagueId", source = "league.id")
    TeamDto toDto(Team team);

    @Mapping(target = "league.id", source = "leagueId")
    Team toEntity(TeamDto dto);

}
