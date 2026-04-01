package ruffinjy.spring_graphql.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruffinjy.spring_graphql.dtos.PlayerStatisticDto;
import ruffinjy.spring_graphql.entities.Match;
import ruffinjy.spring_graphql.entities.Player;
import ruffinjy.spring_graphql.entities.PlayerStatistic;
import ruffinjy.spring_graphql.entities.Team;
import ruffinjy.spring_graphql.mappers.PlayerStatisticMapper;
import ruffinjy.spring_graphql.repositories.MatchRepository;
import ruffinjy.spring_graphql.repositories.PlayerRepository;
import ruffinjy.spring_graphql.repositories.PlayerStatisticRepository;
import ruffinjy.spring_graphql.repositories.TeamRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerStatisticService {

    private final PlayerStatisticRepository playerStatisticRepository;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerStatisticMapper playerStatisticMapper;

    public List<PlayerStatisticDto> findAll() {
        return playerStatisticRepository.findAll().stream()
                .map(playerStatisticMapper::toDto)
                .collect(Collectors.toList());
    }

    public PlayerStatisticDto getById(Long playerStatisticId) {
        PlayerStatistic stat = playerStatisticRepository.findById(playerStatisticId)
                .orElseThrow(() -> new EntityNotFoundException("PlayerStatistic not found"));
        return playerStatisticMapper.toDto(stat);
    }

    @Transactional
    public PlayerStatisticDto create(PlayerStatisticDto playerStatisticDto) {
        PlayerStatistic stat = playerStatisticMapper.toEntity(playerStatisticDto);
        Match match = matchRepository.findById(playerStatisticDto.getMatchId())
                .orElseThrow(() -> new EntityNotFoundException("Match not found"));
        Player player = playerRepository.findById(playerStatisticDto.getPlayerId())
                .orElseThrow(() -> new EntityNotFoundException("Player not found"));
        stat.setMatch(match);
        stat.setPlayer(player);
        if (playerStatisticDto.getTeamId() != null) {
            Team team = teamRepository.findById(playerStatisticDto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team not found"));
            stat.setTeam(team);
        }
        if (stat.getCreatedAt() == null) stat.setCreatedAt(LocalDateTime.now());
        PlayerStatistic saved = playerStatisticRepository.save(stat);
        return playerStatisticMapper.toDto(saved);
    }

    @Transactional
    public PlayerStatisticDto update(Long playerStatisticId, PlayerStatisticDto playerStatisticDto) {
        PlayerStatistic existing = playerStatisticRepository.findById(playerStatisticId)
                .orElseThrow(() -> new EntityNotFoundException("PlayerStatistic not found"));
        if (playerStatisticDto.getMatchId() != null) {
            Match match = matchRepository.findById(playerStatisticDto.getMatchId())
                    .orElseThrow(() -> new EntityNotFoundException("Match not found"));
            existing.setMatch(match);
        }
        if (playerStatisticDto.getPlayerId() != null) {
            Player player = playerRepository.findById(playerStatisticDto.getPlayerId())
                    .orElseThrow(() -> new EntityNotFoundException("Player not found"));
            existing.setPlayer(player);
        }
        if (playerStatisticDto.getTeamId() != null) {
            Team team = teamRepository.findById(playerStatisticDto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team not found"));
            existing.setTeam(team);
        }
        existing.setMinutesPlayed(playerStatisticDto.getMinutesPlayed());
        existing.setGoals(playerStatisticDto.getGoals());
        existing.setAssists(playerStatisticDto.getAssists());
        existing.setYellowCards(playerStatisticDto.getYellowCards());
        existing.setRedCards(playerStatisticDto.getRedCards());
        existing.setShotsOnTarget(playerStatisticDto.getShotsOnTarget());
        PlayerStatistic saved = playerStatisticRepository.save(existing);
        return playerStatisticMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long playerStatisticId) {
        playerStatisticRepository.deleteById(playerStatisticId);
    }

}
