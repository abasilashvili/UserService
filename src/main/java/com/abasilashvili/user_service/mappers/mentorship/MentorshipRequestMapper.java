package com.abasilashvili.user_service.mappers.mentorship;

import com.abasilashvili.user_service.dto.mentorship.MentorshipRequestDto;
import com.abasilashvili.user_service.entity.MentorshipRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        uses = {MenteeDtoMapper.class, MentorDtoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MentorshipRequestMapper {

    MentorshipRequestDto toDto(MentorshipRequest entity);

    MentorshipRequest toEntity(MentorshipRequestDto dto);
}
