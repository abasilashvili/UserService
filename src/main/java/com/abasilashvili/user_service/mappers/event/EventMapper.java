package com.abasilashvili.user_service.mappers.event;

import com.abasilashvili.user_service.dto.event.EventDto;

import com.abasilashvili.user_service.entity.event.Event;
import com.abasilashvili.user_service.mappers.skill.SkillDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {SkillDtoMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "type", target = "eventType")
    @Mapping(source = "status", target = "eventStatus")
    EventDto toDto(Event event);
}
