package ruffinjy.spring_graphql.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ruffinjy.spring_graphql.dtos.AchievementDto;
import ruffinjy.spring_graphql.entities.Achievement;
import ruffinjy.spring_graphql.entities.Player;
import ruffinjy.spring_graphql.entities.Team;
import ruffinjy.spring_graphql.mappers.AchievementMapper;
import ruffinjy.spring_graphql.repositories.AchievementRepository;
import ruffinjy.spring_graphql.repositories.PlayerRepository;
import ruffinjy.spring_graphql.repositories.TeamRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final AchievementMapper achievementMapper;

    public List<AchievementDto> findAll() {
        return achievementRepository.findAll().stream()
                .map(achievementMapper::toDto)
                .collect(Collectors.toList());
    }

    public AchievementDto getById(Long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found"));
        return achievementMapper.toDto(achievement);
    }

    @Transactional
    public AchievementDto create(AchievementDto achievementDto) {
        Achievement achievement = achievementMapper.toEntity(achievementDto);
        if (achievementDto.getPlayerId() != null) {
            Player player = playerRepository.findById(achievementDto.getPlayerId())
                    .orElseThrow(() -> new EntityNotFoundException("Player not found"));
            achievement.setPlayer(player);
        }
        if (achievementDto.getTeamId() != null) {
            Team team = teamRepository.findById(achievementDto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team not found"));
            achievement.setTeam(team);
        }
        if (achievement.getCreatedAt() == null) achievement.setCreatedAt(LocalDateTime.now());
        Achievement saved = achievementRepository.save(achievement);
        return achievementMapper.toDto(saved);
    }

    @Transactional
    public AchievementDto update(Long achievementId, AchievementDto achievementDto) {
        Achievement existing = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new EntityNotFoundException("Achievement not found"));
        existing.setTitle(achievementDto.getTitle());
        existing.setDescription(achievementDto.getDescription());
        existing.setAchievedAt(achievementDto.getAchievedAt());
        if (achievementDto.getPlayerId() != null) {
            Player player = playerRepository.findById(achievementDto.getPlayerId())
                    .orElseThrow(() -> new EntityNotFoundException("Player not found"));
            existing.setPlayer(player);
        }
        if (achievementDto.getTeamId() != null) {
            Team team = teamRepository.findById(achievementDto.getTeamId())
                    .orElseThrow(() -> new EntityNotFoundException("Team not found"));
            existing.setTeam(team);
        }
        Achievement saved = achievementRepository.save(existing);
        return achievementMapper.toDto(saved);
    }

    @Transactional
    public void delete(Long achievementId) {
        achievementRepository.deleteById(achievementId);
    }

}
