package com.abasilashvili.user_service.mappers.education;

import com.abasilashvili.user_service.dto.education.EducationDto;
import com.abasilashvili.user_service.entity.Education;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EducationMapper {

    Education toEntity(EducationDto dto);

    EducationDto toDto(Education entity);
}
