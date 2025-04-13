package com.abasilashvili.user_service.validator.mentorship;

import com.abasilashvili.user_service.entity.MentorshipRequest;
import com.abasilashvili.user_service.entity.RequestStatus;
import com.abasilashvili.user_service.entity.User;
import com.abasilashvili.user_service.exceptions.CooldownPeriodException;
import com.abasilashvili.user_service.exceptions.NoSuchMentorshipRequest;
import com.abasilashvili.user_service.exceptions.UserNotFoundException;
import com.abasilashvili.user_service.repository.UserRepository;
import com.abasilashvili.user_service.repository.mentorship.MentorshipRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MentorshipRequestValidator {

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
        Optional<MentorshipRequest> request = mentorshipRequestRepository.findLatestRequest(userId, mentorId);

        if (request.isPresent()) {
            LocalDateTime threeMonthsCooldown = request.get().getCreatedAt().plusMonths(3);
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
