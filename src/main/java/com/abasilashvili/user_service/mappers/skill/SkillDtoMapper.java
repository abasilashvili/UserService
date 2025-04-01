package com.abasilashvili.user_service.mappers.skill;

import com.abasilashvili.user_service.dto.skill.SkillDto;
import com.abasilashvili.user_service.entity.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SkillDtoMapper {
    SkillDto toDto(Skill skill);
    Skill toEntity(SkillDto skilldto);
}
