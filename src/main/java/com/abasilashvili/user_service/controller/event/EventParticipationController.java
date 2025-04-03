package com.abasilashvili.user_service.controller.event;

import com.abasilashvili.user_service.dto.user.UserDto;
import com.abasilashvili.user_service.service.event.EventParticipationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event-participation")
@Slf4j
public class EventParticipationController {

    private final EventParticipationService eventParticipationService;

    @PostMapping("/{eventId}/{userId}")
    public void registerParticipant(@PathVariable Long eventId, @PathVariable Long userId) {
        log.info("Request to register participant in the event.");
        eventParticipationService.registerParticipant(eventId, userId);
    }

    @DeleteMapping("/{eventId}/{userId}")
    public void unregisterParticipant(@PathVariable Long eventId, @PathVariable Long userId) {
        log.info("Request to de-register participant in the event.");
    }

    @GetMapping("/{eventId}/get-participants")
    public List<UserDto> getParticipants(@PathVariable Long eventId) {
        log.info("Received request to provide with all registered participants to this event.");
        return eventParticipationService.getParticipants(eventId);
    }

    @GetMapping("/{eventId}/count-participants")
    public int countParticipants(@PathVariable Long eventId) {
        log.info("Request to return number of participants");
        return eventParticipationService.countParticipants(eventId);
    }
}
