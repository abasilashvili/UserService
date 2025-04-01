package com.abasilashvili.user_service.service.recommendation;

import com.abasilashvili.user_service.config.properties.recommendation.RecommendationProperties;
import com.abasilashvili.user_service.dto.recommendation.RecommendationRequestDto;
import com.abasilashvili.user_service.dto.recommendation.RejectionDto;
import com.abasilashvili.user_service.dto.recommendation.RequestFilterDto;
import com.abasilashvili.user_service.dto.skill.SkillDto;
import com.abasilashvili.user_service.entity.RequestStatus;
import com.abasilashvili.user_service.entity.Skill;
import com.abasilashvili.user_service.entity.User;
import com.abasilashvili.user_service.entity.recommendation.RecommendationRequest;
import com.abasilashvili.user_service.entity.recommendation.SkillRequest;
import com.abasilashvili.user_service.exceptions.SkillDoesntExistException;
import com.abasilashvili.user_service.exceptions.UserNotFoundException;
import com.abasilashvili.user_service.exceptions.ValidationException;
import com.abasilashvili.user_service.mappers.recommendation.RecommendationRequestMapper;
import com.abasilashvili.user_service.repository.SkillRepository;
import com.abasilashvili.user_service.repository.UserRepository;
import com.abasilashvili.user_service.repository.recommendation.RecommendationRequestRepository;
import com.abasilashvili.user_service.repository.recommendation.SkillRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationRequestService {

    private final RecommendationRequestRepository recommendationRequestRepository;
    private final RecommendationRequestValidator recommendationRequestValidator;
    private final RecommendationRequestMapper recommendationRequestMapper;
    private final SkillRequestRepository skillRequestRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    @Transactional
    public RecommendationRequestDto create(RecommendationRequestDto requestDto) {
        recommendationRequestValidator.validate(requestDto);
        log.info("Received request in Service -requestRecommendation-");
        User requester = userRepository.findById(requestDto.requesterId()).get();
        User receiver = userRepository.findById(requestDto.receiverId()).get();

        RecommendationRequest newRequest = RecommendationRequest.builder()
                .requester(requester)
                .receiver(receiver)
                .message(requestDto.message())
                .status(RequestStatus.PENDING)
                .skills(new ArrayList<>())
                .build();

        RecommendationRequest savedRequest = recommendationRequestRepository.save(newRequest);

        for (SkillDto skillDto : requestDto.skills()) {
            Skill managedSkill = skillRepository.findById(skillDto.getId()).get();

            SkillRequest skillRequest = new SkillRequest();
            skillRequest.setRequest(savedRequest);
            skillRequest.setSkill(managedSkill);

            skillRequestRepository.save(skillRequest);
            savedRequest.addSkillRequest(skillRequest);
        }
        log.info("Saved recommendation.");
        return recommendationRequestMapper.toDto(savedRequest);
    }

    public List<RecommendationRequestDto> getFilteredRequests(RequestFilterDto filterDto) {
        log.info("Received a request to filter recommendations. Service");
        return recommendationRequestRepository.findAll().stream()
                .filter(req -> filterDto.getStatus() == null || req.getStatus().name().equals(filterDto.getStatus()))
                .filter(req -> filterDto.getAfterDate() == null || req.getCreatedAt().isAfter(filterDto.getAfterDate()))
                .filter(req -> filterDto.getUserId() == null || req.getId() == filterDto.getUserId())
                .map(recommendationRequestMapper::toDto)
                .toList();
    }

    public RecommendationRequestDto getRequestById(Long id) {
        log.info("Received a request to find user with id {}. Service", id);
        return recommendationRequestRepository.findById(id)
                .map(recommendationRequestMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("Такого пользователя не существует " + id));
    }

    public ResponseEntity<String> rejectRequest(Long id, RejectionDto rejection) {
        RecommendationRequest request = recommendationRequestRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pending request with id {} not found", id);
                    return new ValidationException("Request not found");
                });
        log.info("Rejection request. Service.");
        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(rejection.getReason());

        recommendationRequestRepository.save(request);

        log.info("Request rejected with reason {}", rejection.getReason());
        return ResponseEntity.ok("Request successfully rejected");
    }
}

@Component
@RequiredArgsConstructor
@Slf4j
class RecommendationRequestValidator {

    private final RecommendationRequestRepository recommendationRequestRepository;
    private final UserRepository userRepository;
    private final RecommendationProperties recommendationProperties;
    private final SkillRepository skillRepository;

    void validate(RecommendationRequestDto dto) {
        try {
            validateUsersExist(dto);
            validateSixMonthPeriod(dto);
            checkSkillsExistence(dto);
        } catch (UserNotFoundException | ValidationException | SkillDoesntExistException ex) {
            log.warn(ex.getMessage());
        }
    }

    private void validateUsersExist(RecommendationRequestDto dto) throws UserNotFoundException {
        List<Long> userIds = List.of(dto.requesterId(), dto.receiverId());

        for (long userId : userIds) {
            if (!userRepository.existsById(userId)) {
                log.warn("No such user");
                throw new UserNotFoundException("Пользователя не существует.");
            }
        }
    }

    private void validateSixMonthPeriod(RecommendationRequestDto dto) throws ValidationException {
        Optional<RecommendationRequest> recommendationRequest = recommendationRequestRepository
                .findLatestPendingRequest(dto.requesterId(), dto.receiverId());
        if (recommendationRequest.isEmpty()) {
            log.info("Validation pass. No previous requests");
        } else if (LocalDateTime.now().minusMonths(recommendationProperties.getCooldownMonths())
                .isBefore(recommendationRequest.get().getCreatedAt())) {
            log.warn("6 month period didn't pass yet.");
            throw new ValidationException("Ещё не прошло 6 месяцев с предыдущего запроса.");
        }
    }

    private void checkSkillsExistence(RecommendationRequestDto dto) {
        List<SkillDto> skillsOffered = dto.skills();
        for (SkillDto skill : skillsOffered) {
            if (!skillRepository.existsByTitle(skill.getTitle())) {
                log.warn("Such skill doesn't exist.");
                throw new SkillDoesntExistException("Такого Скилла не существует.");
            }
        }
    }
}
