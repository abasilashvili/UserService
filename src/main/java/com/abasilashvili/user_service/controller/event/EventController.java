package com.abasilashvili.user_service.controller.event;

import com.abasilashvili.user_service.dto.event.EventDto;
import com.abasilashvili.user_service.dto.event.EventFilterDto;
import com.abasilashvili.user_service.service.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/event")
@Slf4j
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/create")
    public EventDto create(@RequestBody EventDto event) {
        log.info("Received request to create new Event.");
        return eventService.create(event);
    }

    @GetMapping("/get/{eventId}")
    public EventDto getEvent(Long eventId) {
        log.info("Received request to get Event by Id.");
        return eventService.getEvent(eventId);
    }

    @GetMapping("/get-with-filters")
    public List<EventDto> getEventsByFilter(@RequestBody EventFilterDto filter) {
        log.info("Received request to get events by provided filters.");
        return eventService.getEventsByFilter(filter);
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        log.info("Received request to delete event.");
        return eventService.deleteEvent(eventId);
    }

    @PutMapping("/update")
    public EventDto updateEvent(@RequestBody EventDto event) {
        log.info("Received request to update event.");
        return eventService.updateEvent(event);
    }

    @GetMapping("/get-by-user-id/{userId}")
    public List<EventDto> getEventsByUserId(@PathVariable Long userId) {
        log.info("Received request to get all events created by user.");
        return eventService.getEventsByUserId(userId);
    }

    @GetMapping("/get-paticipated-events/{userId}")
    public List<EventDto> getParticipatedEvents(@PathVariable Long userId) {
        log.info("Received request to get all events user takes participation");
        return eventService.getParticipatedEvents(userId);
    }
}
