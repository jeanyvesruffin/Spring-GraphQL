package ruffinjy.spring_graphql.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruffinjy.spring_graphql.dtos.MatchDto;
import ruffinjy.spring_graphql.entities.League;
import ruffinjy.spring_graphql.entities.Match;
import ruffinjy.spring_graphql.entities.Team;
import ruffinjy.spring_graphql.mappers.MatchMapper;
import ruffinjy.spring_graphql.repositories.LeagueRepository;
import ruffinjy.spring_graphql.repositories.MatchRepository;
import ruffinjy.spring_graphql.repositories.TeamRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final MatchMapper matchMapper;

    public List<MatchDto> findAll() {
        return matchRepository.findAll().stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    public MatchDto getById(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("Match not found"));
        return matchMapper.toDto(match);
    }

    @Transactional
    public MatchDto create(MatchDto matchDto) {
        if (matchDto.getHomeTeamId() != null && matchDto.getHomeTeamId().equals(matchDto.getAwayTeamId())) {
            throw new IllegalArgumentException("Home and away teams must be different");
        }

        Match match = matchMapper.toEntity(matchDto);
        if (matchDto.getLeagueId() != null) {
            League league = leagueRepository.findById(matchDto.getLeagueId())
                    .orElseThrow(() -> new EntityNotFoundException("League not found"));
            match.setLeague(league);
        }
        Team home = teamRepository.findById(matchDto.getHomeTeamId())
                .orElseThrow(() -> new EntityNotFoundException("Home team not found"));
        Team away = teamRepository.findById(matchDto.getAwayTeamId())
                .orElseThrow(() -> new EntityNotFoundException("Away team not found"));
        match.setHomeTeam(home);
        match.setAwayTeam(away);
        if (match.getCreatedAt() == null)
            match.setCreatedAt(LocalDateTime.now());
        Match saved = matchRepository.save(match);
        return matchMapper.toDto(saved);
    }

    @Transactional
    public MatchDto update(Long matchId, MatchDto matchDto) {
        Match existing = matchRepository.findById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("Match not found"));
        if (matchDto.getHomeTeamId() != null && matchDto.getHomeTeamId().equals(matchDto.getAwayTeamId())) {
            throw new IllegalArgumentException("Home and away teams must be different");
        }
        existing.setSeason(matchDto.getSeason());
        existing.setMatchDate(matchDto.getMatchDate());
        existing.setHomeScore(matchDto.getHomeScore());
        existing.setAwayScore(matchDto.getAwayScore());
        existing.setStatus(matchDto.getStatus());
        existing.setVenue(matchDto.getVenue());
        if (matchDto.getLeagueId() != null) {
            League league = leagueRepository.findById(matchDto.getLeagueId())
                    .orElseThrow(() -> new EntityNotFoundException("League not found"));
            existing.setLeague(league);
        }
        if (matchDto.getHomeTeamId() != null) {
            Team home = teamRepository.findById(matchDto.getHomeTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Home team not found"));
            existing.setHomeTeam(home);
        }
        if (matchDto.getAwayTeamId() != null) {
            Team away = teamRepository.findById(matchDto.getAwayTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Away team not found"));
            existing.setAwayTeam(away);
        }
        Match saved = matchRepository.save(existing);
        return matchMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long matchId) {
        matchRepository.deleteById(matchId);
    }

}
