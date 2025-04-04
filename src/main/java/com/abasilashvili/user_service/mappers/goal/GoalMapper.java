package com.abasilashvili.user_service.mappers.goal;

import com.abasilashvili.user_service.dto.goal.GoalDto;
import com.abasilashvili.user_service.entity.goal.Goal;
import com.abasilashvili.user_service.mappers.skill.SkillDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {SkillDtoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalMapper {

    GoalDto toDto(Goal goal);
}
