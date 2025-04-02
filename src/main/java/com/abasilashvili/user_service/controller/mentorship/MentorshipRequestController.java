package com.abasilashvili.user_service.controller.mentorship;

import com.abasilashvili.user_service.dto.mentorship.MentorshipRequestDto;
import com.abasilashvili.user_service.dto.mentorship.RequestMentorshipFilterDto;
import com.abasilashvili.user_service.service.mentorship.MentorshipRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mentorship/request")
@Slf4j
@RequiredArgsConstructor
public class MentorshipRequestController {

    private final MentorshipRequestService mentorshipRequestService;

    @PostMapping("/ask-for-mentorship")
    public ResponseEntity<String> mentorshipRequest(@RequestBody MentorshipRequestDto dto) {
        log.info("Created new mentorship request from user {} to mentor {}.",
                dto.getRequester().getUsername(), dto.getReceiver().getUsername());
        return mentorshipRequestService.mentorshipRequest(dto);
    }

    @GetMapping("/filtered-requests")
    public List<MentorshipRequestDto> getRequests(@RequestBody @Valid RequestMentorshipFilterDto filters) {
        log.info("Filtered requests. Controller");
        return mentorshipRequestService.getRequests(filters);
    }

    @PutMapping("/accept-request/{requestId}")
    public ResponseEntity<String> acceptRequest(@PathVariable Long requestId) {
        log.info("Accepted request. Controller");
        return mentorshipRequestService.acceptRequest(requestId);
    }

    @PutMapping("/reject-request/{requestId}")
    public ResponseEntity<String> rejectRequest(@PathVariable Long requestId) {
        log.info("Declined request. Controller");
        return mentorshipRequestService.rejectRequest(requestId);
    }
}
