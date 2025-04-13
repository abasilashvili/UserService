package com.abasilashvili.user_service.validator.event;

import com.abasilashvili.user_service.dto.event.EventDto;
import com.abasilashvili.user_service.dto.skill.SkillDto;
import com.abasilashvili.user_service.entity.User;
import com.abasilashvili.user_service.exceptions.SkillDoesntExistException;
import com.abasilashvili.user_service.exceptions.UserNotFoundException;
import com.abasilashvili.user_service.exceptions.ValidationException;
import com.abasilashvili.user_service.mappers.skill.SkillDtoMapper;
import com.abasilashvili.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventServiceValidation {

    private final UserRepository userRepository;
    private final SkillDtoMapper skillDtoMapper;

    public void validateEvent(EventDto eventDto) {
        log.info("Starting validation.");
        validateTitle(eventDto.getTitle());
        validateStartDate(eventDto.getStartDate());
        validateOwner(eventDto.getOwnerId());
        validateSkills(eventDto);
        log.info("Validation passed.");
    }

    public void validateTitle(String title) {
        if (title.isEmpty()) {
            log.warn("Event must have title.");
            throw new ValidationException("Event must have title.");
        }
    }

    public void validateStartDate(LocalDateTime startDate) {
        if (startDate == null) {
            log.warn("Event must have start date.");
            throw new ValidationException("Event must have start date.");
        }
    }

    public void validateOwner(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Event must have valid owner."));
    }

    public void validateSkills(EventDto dto) {
        List<SkillDto> userSkills = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException("User not found."))
                .getSkills().stream()
                .map(skillDtoMapper::toDto)
                .toList();

        List<SkillDto> eventsSkills = dto.getRelatedSkills();

        if (!new HashSet<>(userSkills).containsAll(eventsSkills)) {
            log.warn("You can't start event because you don't have enough skills yet.");
            throw new SkillDoesntExistException("You can't start event because you don't have enough skills yet.");
        }
    }
}
