package com.abasilashvili.user_service.service.goal;

import com.abasilashvili.user_service.dto.goal.GoalDto;
import com.abasilashvili.user_service.dto.skill.SkillDto;
import com.abasilashvili.user_service.entity.Skill;
import com.abasilashvili.user_service.entity.goal.Goal;
import com.abasilashvili.user_service.exceptions.AlreadyExistsException;
import com.abasilashvili.user_service.mappers.goal.GoalMapper;
import com.abasilashvili.user_service.mappers.skill.SkillDtoMapper;
import com.abasilashvili.user_service.repository.SkillRepository;
import com.abasilashvili.user_service.repository.goal.GoalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalService {

    private final GoalMapper goalMapper;
    private final GoalRepository goalRepository;
    private final SkillRepository skillRepository;

    public GoalDto createGoal(Long userId, GoalDto dto) {
        if (!checkActiveGoals(userId)) {
            throw new AlreadyExistsException("You already have 3 active goals.");
        }

        if (dto.getParentGoal() != null) {
            goalRepository.create(dto.getTitle(), dto.getDescription(), dto.getParentGoal().getId());
        }

        Goal newGoal = Goal.builder()
                .createdAt(LocalDateTime.now())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .deadline(dto.getDeadline())
                .build();

        goalRepository.save(newGoal);
        log.info("Saved new goal to database.");

        return goalMapper.toDto(newGoal);
    }

    private boolean checkActiveGoals(Long userId) {
        log.info("Checking if user has 3 active goals.");
        return goalRepository.countActiveGoalsPerUser(userId) < 3;
    }
}
