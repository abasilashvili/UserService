package com.abasilashvili.user_service.service.mentorship;

import com.abasilashvili.user_service.dto.mentorship.MentorshipRequestDto;
import com.abasilashvili.user_service.dto.mentorship.RequestMentorshipFilterDto;
import com.abasilashvili.user_service.entity.MentorshipRequest;
import com.abasilashvili.user_service.entity.RequestStatus;
import com.abasilashvili.user_service.entity.User;
import com.abasilashvili.user_service.exceptions.CooldownPeriodException;
import com.abasilashvili.user_service.exceptions.NoSuchMentorshipRequest;
import com.abasilashvili.user_service.exceptions.UserNotFoundException;
import com.abasilashvili.user_service.mappers.mentorship.MentorshipRequestMapper;
import com.abasilashvili.user_service.repository.UserRepository;
import com.abasilashvili.user_service.repository.mentorship.MentorshipRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

@Component
@RequiredArgsConstructor
@Slf4j
class MentorshipRequestValidator {

    private final UserRepository userRepository;
    private final MentorshipRequestRepository mentorshipRequestRepository;

    public void validate(Long userId, Long mentorId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist"));

        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new UserNotFoundException("Requested mentor doesn't exist"));

        log.info("User validation passed");
        validateThreeMonthCooldown(userId, mentorId);
    }

    public void validateThreeMonthCooldown(Long userId, Long mentorId) {
        Optional<MentorshipRequest> requests = mentorshipRequestRepository.findLatestRequest(userId, mentorId);

        if (requests.isPresent()) {
            LocalDateTime threeMonthsCooldown = requests.get().getCreatedAt().plusMonths(3);
            if (!LocalDateTime.now().isAfter(threeMonthsCooldown)) {
                log.warn("Three month haven't passed yet.");
                throw new CooldownPeriodException("Three month haven't passed yet.");
            }
        }
    }

    public MentorshipRequest validateRequestExistence(Long id) {
        return mentorshipRequestRepository.findById(id)
                .orElseThrow(() -> new NoSuchMentorshipRequest("Запроса с таким id не существует."));
    }

    public void updateRequestStatus(RequestStatus status, MentorshipRequest request) {
        request.setStatus(status);
        mentorshipRequestRepository.save(request);
        log.info("Updated request status to {}", status);
    }
}
