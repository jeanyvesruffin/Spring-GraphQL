package ruffinjy.spring_graphql.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ruffinjy.spring_graphql.dtos.MatchDto;
import ruffinjy.spring_graphql.entities.Match;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    @Mapping(target = "leagueId", source = "league.id")
    @Mapping(target = "homeTeamId", source = "homeTeam.id")
    @Mapping(target = "awayTeamId", source = "awayTeam.id")
    MatchDto toDto(Match match);

    @Mapping(target = "league.id", source = "leagueId")
    @Mapping(target = "homeTeam.id", source = "homeTeamId")
    @Mapping(target = "awayTeam.id", source = "awayTeamId")
    Match toEntity(MatchDto dto);

}
