package ruffinjy.spring_graphql.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruffinjy.spring_graphql.dtos.TeamDto;
import ruffinjy.spring_graphql.entities.League;
import ruffinjy.spring_graphql.entities.Team;
import ruffinjy.spring_graphql.mappers.TeamMapper;
import ruffinjy.spring_graphql.repositories.LeagueRepository;
import ruffinjy.spring_graphql.repositories.TeamRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final LeagueRepository leagueRepository;
    private final TeamMapper teamMapper;

    public List<TeamDto> findAll() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toDto)
                .collect(Collectors.toList());
    }

    public TeamDto getById(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
        return teamMapper.toDto(team);
    }

    @Transactional
    public TeamDto create(TeamDto teamDto) {
        Team team = teamMapper.toEntity(teamDto);
        if (teamDto.getLeagueId() != null) {
            League league = leagueRepository.findById(teamDto.getLeagueId())
                    .orElseThrow(() -> new EntityNotFoundException("League not found"));
            team.setLeague(league);
        }
        if (team.getCreatedAt() == null) team.setCreatedAt(LocalDateTime.now());
        Team saved = teamRepository.save(team);
        return teamMapper.toDto(saved);
    }

    @Transactional
    public TeamDto update(Long teamId, TeamDto teamDto) {
        Team existing = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
        existing.setName(teamDto.getName());
        existing.setShortName(teamDto.getShortName());
        existing.setFoundedYear(teamDto.getFoundedYear());
        existing.setCity(teamDto.getCity());
        existing.setStadium(teamDto.getStadium());
        if (teamDto.getLeagueId() != null) {
            League league = leagueRepository.findById(teamDto.getLeagueId())
                    .orElseThrow(() -> new EntityNotFoundException("League not found"));
            existing.setLeague(league);
        }
        Team saved = teamRepository.save(existing);
        return teamMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long teamId) {
        teamRepository.deleteById(teamId);
    }

}
