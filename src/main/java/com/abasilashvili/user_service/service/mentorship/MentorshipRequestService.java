package com.abasilashvili.user_service.service.mentorship;

import com.abasilashvili.user_service.dto.mentorship.MentorshipRequestDto;
import com.abasilashvili.user_service.dto.mentorship.RequestMentorshipFilterDto;
import com.abasilashvili.user_service.entity.MentorshipRequest;
import com.abasilashvili.user_service.entity.RequestStatus;
import com.abasilashvili.user_service.mappers.mentorship.MentorshipRequestMapper;
import com.abasilashvili.user_service.repository.mentorship.MentorshipRequestRepository;
import com.abasilashvili.user_service.validator.mentorship.MentorshipRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.springframework.http.ResponseEntity.ok;

@Service
@RequiredArgsConstructor
@Slf4j
public class MentorshipRequestService {

    private final MentorshipRequestRepository mentorshipRequestRepository;
    private final MentorshipRequestValidator validator;
    private final MentorshipRequestMapper mentorshipRequestMapper;

    public ResponseEntity<String> mentorshipRequest(MentorshipRequestDto requestDto) {
        Long userId = requestDto.getRequester().getId();
        Long mentorId = requestDto.getReceiver().getId();
        String motivationLetter = requestDto.getMotivationLetter();

        validator.validate(userId, mentorId);
        log.info("Users validated.");

        mentorshipRequestRepository.create(userId, mentorId, motivationLetter);
        log.info("Created new mentorship request from user {} to mentor {}.", userId, mentorId);

        return ok("Request sent.");
    }

    public List<MentorshipRequestDto> getRequests(RequestMentorshipFilterDto filterDto) {
        log.info("Received request to filter mentorship requests and return them. Service.");
        return StreamSupport
                .stream(mentorshipRequestRepository.findAll().spliterator(), false)
                .filter(req -> filterDto.getStatus() == null || Objects.equals(filterDto.getStatus(), req.getStatus().toString()))
                .filter(req -> filterDto.getCreatedAfter() == null || req.getCreatedAt().isAfter(filterDto.getCreatedAfter()))
                .filter(req -> filterDto.getRejectionReason() == null || Objects.equals(req.getRejectionReason(), filterDto.getRejectionReason()))
                .map(mentorshipRequestMapper::toDto)
                .toList();
    }

    public ResponseEntity<String> acceptRequest(Long id) {
        MentorshipRequest requestToUpdate = validator.validateRequestExistence(id);
        validator.updateRequestStatus(RequestStatus.ACCEPTED, requestToUpdate);

        return ok("Request Accepted.");
    }

    public ResponseEntity<String> rejectRequest(Long id) {
        MentorshipRequest requestToUpdate = validator.validateRequestExistence(id);
        validator.updateRequestStatus(RequestStatus.REJECTED, requestToUpdate);

        return ResponseEntity.ok("Request Rejected");
    }
}

