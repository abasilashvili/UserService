package com.abasilashvili.user_service.service.event;

import com.abasilashvili.user_service.dto.user.UserDto;
import com.abasilashvili.user_service.exceptions.AlreadyExistsException;
import com.abasilashvili.user_service.mappers.user.UserMapper;
import com.abasilashvili.user_service.repository.event.EventParticipationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventParticipationService {

    private final EventParticipationRepository eventParticipationRepository;
    private final UserMapper userMapper;

    public void registerParticipant(Long eventId, Long userId) {
        if (checkIfAlreadyRegistered(eventId, userId)) {
            throw new AlreadyExistsException("You are already registered on this event.");
        }
        log.info("Request to register participant in the event.");
        eventParticipationRepository.register(eventId, userId);
    }

    public void unregisterParticipant(Long eventId, Long userId) {
        if (!checkIfAlreadyRegistered(eventId, userId)) {
            throw new AlreadyExistsException("You are not registered on this event.");
        }
        log.info("Request to de-register participant in the event.");
        eventParticipationRepository.unregister(eventId, userId);
    }

    public List<UserDto> getParticipants(Long eventId) {
        log.info("Received request to provide with all registered participants to this event.");
        return eventParticipationRepository.findAllParticipantsByEventId(eventId)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public int countParticipants(Long eventId) {
        log.info("Request to return number of participants");
        return eventParticipationRepository.countParticipants(eventId);
    }

    private boolean checkIfAlreadyRegistered(Long eventId, Long userId) {
        return eventParticipationRepository.findAllParticipantsByEventId(eventId)
                .stream()
                .anyMatch(user -> Objects.equals(user.getId(), userId));
    }
}
