package com.abasilashvili.user_service.validator.recommendations;

import com.abasilashvili.user_service.config.properties.recommendation.RecommendationProperties;
import com.abasilashvili.user_service.dto.recommendation.RecommendationRequestDto;
import com.abasilashvili.user_service.dto.skill.SkillDto;
import com.abasilashvili.user_service.entity.recommendation.RecommendationRequest;
import com.abasilashvili.user_service.exceptions.SkillDoesntExistException;
import com.abasilashvili.user_service.exceptions.UserNotFoundException;
import com.abasilashvili.user_service.exceptions.ValidationException;
import com.abasilashvili.user_service.repository.SkillRepository;
import com.abasilashvili.user_service.repository.UserRepository;
import com.abasilashvili.user_service.repository.recommendation.RecommendationRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationRequestValidator {

    private final RecommendationRequestRepository recommendationRequestRepository;
    private final UserRepository userRepository;
    private final RecommendationProperties recommendationProperties;
    private final SkillRepository skillRepository;

    public void validate(RecommendationRequestDto dto) {
        try {
            validateUsersExist(dto);
            validateSixMonthPeriod(dto);
            checkSkillsExistence(dto);
        } catch (UserNotFoundException | ValidationException | SkillDoesntExistException ex) {
            log.warn(ex.getMessage());
            throw ex;
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
