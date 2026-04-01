package ruffinjy.spring_graphql.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ruffinjy.spring_graphql.dtos.PlayerDto;
import ruffinjy.spring_graphql.entities.Player;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mapping(target = "teamId", source = "team.id")
    PlayerDto toDto(Player player);

    @Mapping(target = "team.id", source = "teamId")
    Player toEntity(PlayerDto dto);

}
