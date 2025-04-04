package com.abasilashvili.user_service.dto.goal;

import com.abasilashvili.user_service.dto.skill.SkillDto;
import com.abasilashvili.user_service.entity.goal.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalDto {

    private Long id;

    private String title;

    private String description;

    private GoalDto parentGoal;

    private GoalStatus status;

    private LocalDateTime deadline;

    private LocalDateTime createdAt;

    private List<SkillDto> skillsToAchieve;

}
