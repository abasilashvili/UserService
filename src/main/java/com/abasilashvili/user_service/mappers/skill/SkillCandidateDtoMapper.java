package com.abasilashvili.user_service.mappers.skill;

import com.abasilashvili.user_service.dto.skill.SkillCandidateDto;
import com.abasilashvili.user_service.dto.skill.SkillDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SkillCandidateDtoMapper {
    SkillCandidateDto toCandidate(SkillDto skillDto);
}
