package com.abasilashvili.user_service.controller.mentorship;

import com.abasilashvili.user_service.dto.mentorship.MenteeDto;
import com.abasilashvili.user_service.dto.mentorship.MentorDto;
import com.abasilashvili.user_service.service.mentorship.MentorshipService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mentorship")
@RequiredArgsConstructor
@Slf4j
public class MentorshipController {

    private final MentorshipService mentorshipService;

    @GetMapping("/all-mentees/{id}")
    public List<MenteeDto> getMentees(@PathVariable @Min(1) Long id) {
        log.info("Received request to get all mentees for user with id {}. Controller", id);
        return mentorshipService.getMentees(id);
    }

    @GetMapping("/all-mentors/{id}")
    public List<MentorDto> getMentors(@PathVariable @Min(1) Long id) {
        log.info("Received request to get all mentors for user with id {}. Controller", id);
        return mentorshipService.getMentors(id);
    }

    @DeleteMapping("/delete-mentee")
    public ResponseEntity<String> deleteMentee(@RequestBody Long menteeId, Long mentorId) {
        log.info("Received request to delete mentee from mentor {}. Controller", mentorId);
        return mentorshipService.deleteMentee(menteeId, mentorId);
    }

    @DeleteMapping("/delete-mentor")
    public ResponseEntity<String> deleteMentor(@RequestBody Long menteeId, Long mentorId) {
        log.info("Received request to delete mentor from mentee {}. Controller", menteeId);
        return mentorshipService.deleteMentor(menteeId, mentorId);
    }

}
