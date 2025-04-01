package com.abasilashvili.user_service.mappers.mentorship;

import com.abasilashvili.user_service.dto.mentorship.MentorDto;
import com.abasilashvili.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MentorDtoMapper {
    MentorDto toDto(User user);
}
