package com.abasilashvili.user_service.service.event;

import com.abasilashvili.user_service.dto.event.EventDto;
import com.abasilashvili.user_service.dto.event.EventFilterDto;
import com.abasilashvili.user_service.entity.Skill;
import com.abasilashvili.user_service.entity.User;
import com.abasilashvili.user_service.entity.event.Event;
import com.abasilashvili.user_service.entity.event.EventStatus;
import com.abasilashvili.user_service.entity.event.EventType;
import com.abasilashvili.user_service.exceptions.ValidationException;
import com.abasilashvili.user_service.mappers.event.EventMapper;
import com.abasilashvili.user_service.mappers.skill.SkillDtoMapper;
import com.abasilashvili.user_service.repository.UserRepository;
import com.abasilashvili.user_service.repository.event.EventRepository;
import com.abasilashvili.user_service.validator.event.EventServiceValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventServiceValidation validator;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final SkillDtoMapper skillDtoMapper;
    private final EventMapper eventMapper;

    @Transactional
    public EventDto create(EventDto eventDto) {
        validator.validateEvent(eventDto);
        log.info("Starting to created new Event");
        User eventOwner = userRepository.findById(eventDto.getOwnerId()).get();
        List<Skill> skills = eventDto.getRelatedSkills()
                .stream()
                .map(skillDtoMapper::toEntity)
                .toList();
        Event newEvent = Event.builder()
                .title(eventDto.getTitle())
                .createdAt(LocalDateTime.now())
                .startDate(eventDto.getStartDate())
                .endDate(eventDto.getEndDate())
                .owner(eventOwner)
                .description(eventDto.getDescription())
                .relatedSkills(skills)
                .location(eventDto.getLocation())
                .maxAttendees(eventDto.getMaxAttendees())
                .type(eventDto.getEventType())
                .status(eventDto.getEventStatus())
                .build();

        eventRepository.save(newEvent);
        log.info("Saved to DB.");

        return eventMapper.toDto(newEvent);
    }

    public EventDto getEvent(Long id) {
        log.info("Receiver request to get Event by Id.");
        return eventMapper.toDto(eventRepository.findById(id)
                .orElseThrow(() -> new ValidationException("No event with this id.")));
    }

    public List<EventDto> getEventsByFilter(EventFilterDto filter) {
        return eventRepository.findAll().stream()
                .map(eventMapper::toDto)
                .filter(event -> matchesFilter(event, filter))
                .toList();
    }

    public ResponseEntity<String> deleteEvent(Long eventId) {
        log.info("Received request to delete event.");
        eventRepository.deleteById(eventId);
        return ResponseEntity.ok("Event was deleted.");
    }

    @Transactional
    public EventDto updateEvent(EventDto eventDto) {
        log.info("Received request to update event.");
        validator.validateEvent(eventDto);
        Event eventToUpdate = eventRepository.findById(eventDto.getId()).get();
        User owner = userRepository.findById(eventDto.getOwnerId()).get();

        eventToUpdate.setTitle(eventDto.getTitle());
        eventToUpdate.setStartDate(eventDto.getStartDate());
        eventToUpdate.setEndDate(eventDto.getEndDate());
        eventToUpdate.setOwner(owner);
        eventToUpdate.setDescription(eventDto.getDescription());
        eventToUpdate.setMaxAttendees(eventDto.getMaxAttendees());
        eventToUpdate.setLocation(eventDto.getLocation());
        eventToUpdate.setType(eventDto.getEventType());
        eventToUpdate.setStatus(eventDto.getEventStatus());

        Event updatedEvent = eventRepository.save(eventToUpdate);
        log.info("Saved updated event to DB");

        return eventMapper.toDto(updatedEvent);
    }

    public List<EventDto> getEventsByUserId(Long userId) {
        log.info("Received request to get all events created by user.");
        return eventRepository.findAllByUserId(userId).stream()
                .map(eventMapper::toDto)
                .toList();
    }

    public List<EventDto> getParticipatedEvents(Long userId) {
        log.info("Received request to get all events user takes participation");
        return eventRepository.findParticipatedEventsByUserId(userId)
                .stream()
                .map(eventMapper::toDto)
                .toList();
    }

    private boolean matchesFilter(EventDto eventDto, EventFilterDto filter) {
        return matchesTitle(eventDto, filter.getTitlePattern()) &&
                matchesLocation(eventDto, filter.getLocationPattern()) &&
                matchesMaxAttendees(eventDto, filter.getMaxAttendees()) &&
                matchesEventType(eventDto, filter.getType()) &&
                matchesEventStatus(eventDto, filter.getStatus());
    }

    private boolean matchesTitle(EventDto eventDto, String title) {
        return title == null || title.isEmpty() ||
                eventDto.getTitle().toLowerCase().contains(title.toLowerCase());
    }

    private boolean matchesLocation(EventDto eventDto, String location) {
        return location == null || location.isEmpty() ||
                (eventDto.getLocation() != null && eventDto.getLocation().contains(location));
    }

    private boolean matchesMaxAttendees(EventDto eventDto, int max) {
        return eventDto.getMaxAttendees() <= max;
    }

    private boolean matchesEventType(EventDto eventDto, EventType type) {
        return eventDto.getEventType() == type;
    }

    private boolean matchesEventStatus(EventDto eventDto, EventStatus status) {
        return eventDto.getEventStatus() == status;
    }
}

