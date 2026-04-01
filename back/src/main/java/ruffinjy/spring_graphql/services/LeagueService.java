package ruffinjy.spring_graphql.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ruffinjy.spring_graphql.dtos.LeagueDto;
import ruffinjy.spring_graphql.entities.League;
import ruffinjy.spring_graphql.mappers.LeagueMapper;
import ruffinjy.spring_graphql.repositories.LeagueRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final LeagueMapper leagueMapper;

    public List<LeagueDto> findAll() {
        return leagueRepository.findAll().stream()
                .map(leagueMapper::toDto)
                .collect(Collectors.toList());
    }

    public LeagueDto getById(Long leagueId) {
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new EntityNotFoundException("League not found"));
        return leagueMapper.toDto(league);
    }

    @Transactional
    public LeagueDto create(LeagueDto leagueDto) {
        League league = leagueMapper.toEntity(leagueDto);
        league.setId(null);
        if (league.getCreatedAt() == null) league.setCreatedAt(LocalDateTime.now());
        League saved = leagueRepository.save(league);
        return leagueMapper.toDto(saved);
    }

    @Transactional
    public LeagueDto update(Long leagueId, LeagueDto leagueDto) {
        League existing = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new EntityNotFoundException("League not found"));
        existing.setName(leagueDto.getName());
        existing.setCode(leagueDto.getCode());
        existing.setCountry(leagueDto.getCountry());
        existing.setUpdatedAt(LocalDateTime.now());
        League saved = leagueRepository.save(existing);
        return leagueMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long leagueId) {
        leagueRepository.deleteById(leagueId);
    }

}
