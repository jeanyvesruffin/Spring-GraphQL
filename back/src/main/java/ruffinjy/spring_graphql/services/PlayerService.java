package ruffinjy.spring_graphql.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruffinjy.spring_graphql.dtos.PlayerDto;
import ruffinjy.spring_graphql.entities.Player;
import ruffinjy.spring_graphql.entities.Team;
import ruffinjy.spring_graphql.mappers.PlayerMapper;
import ruffinjy.spring_graphql.repositories.PlayerRepository;
import ruffinjy.spring_graphql.repositories.TeamRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerMapper playerMapper;

    public List<PlayerDto> findAll() {
        return playerRepository.findAll().stream()
                .map(playerMapper::toDto)
                .collect(Collectors.toList());
    }

    public PlayerDto getById(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found"));
        return playerMapper.toDto(player);
    }

    @Transactional
    public PlayerDto create(PlayerDto playerDto) {
        Player player = playerMapper.toEntity(playerDto);
        if (playerDto.getTeamId() != null) {
            Team team = teamRepository.findById(playerDto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team not found"));
            player.setTeam(team);
        }
        if (player.getCreatedAt() == null) player.setCreatedAt(LocalDateTime.now());
        Player saved = playerRepository.save(player);
        return playerMapper.toDto(saved);
    }

    @Transactional
    public PlayerDto update(Long playerId, PlayerDto playerDto) {
        Player existing = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found"));
        existing.setFirstName(playerDto.getFirstName());
        existing.setLastName(playerDto.getLastName());
        existing.setBirthdate(playerDto.getBirthdate());
        existing.setPosition(playerDto.getPosition());
        existing.setNumber(playerDto.getNumber());
        existing.setNationality(playerDto.getNationality());
        existing.setContractUntil(playerDto.getContractUntil());
        if (playerDto.getTeamId() != null) {
            Team team = teamRepository.findById(playerDto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team not found"));
            existing.setTeam(team);
        }
        Player saved = playerRepository.save(existing);
        return playerMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long playerId) {
        playerRepository.deleteById(playerId);
    }

}
