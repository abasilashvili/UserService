package com.abasilashvili.user_service.mappers.mentorship;

import com.abasilashvili.user_service.dto.mentorship.MenteeDto;
import com.abasilashvili.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenteeDtoMapper {
    MenteeDto toDto(User user);
}
