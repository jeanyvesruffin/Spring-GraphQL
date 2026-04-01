package ruffinjy.spring_graphql.mappers;

import org.mapstruct.Mapper;

import ruffinjy.spring_graphql.dtos.LeagueDto;
import ruffinjy.spring_graphql.entities.League;

@Mapper(componentModel = "spring")
public interface LeagueMapper {

    LeagueDto toDto(League league);

    League toEntity(LeagueDto dto);

}
