package com.abasilashvili.user_service.service.validatortests;

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
import com.abasilashvili.user_service.validator.recommendations.RecommendationRequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationRequestValidatorTest {

    @Mock
    private RecommendationRequestRepository recommendationRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecommendationProperties recommendationProperties;

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private RecommendationRequestValidator validator;

    private RecommendationRequestDto createValidDto() {
        return new RecommendationRequestDto(1, "Java",
                List.of(new SkillDto(1L, "Java"), new SkillDto(2L, "Tests")),
                "pending request", 1L, 2L,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    public void testShouldThrowWhenRequesterNotExists() {
        RecommendationRequestDto dto = createValidDto();

        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class,
                () -> validator.validate(dto));
        verify(userRepository, times(1)).existsById(1L);
    }

    @Test
    public void testShouldThrowWhenSixMonthPeriod() {
        RecommendationRequestDto dto = createValidDto();
        RecommendationRequest existingRequest = new RecommendationRequest();
        existingRequest.setCreatedAt(LocalDateTime.now().minusMonths(5));

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(recommendationProperties.getCooldownMonths()).thenReturn(6);
        when(recommendationRequestRepository.findLatestPendingRequest(anyLong(), anyLong()))
                .thenReturn(Optional.of(existingRequest));

        assertThrows(ValidationException.class, () -> validator.validate(dto));
        verify(recommendationRequestRepository, times(1))
                .findLatestPendingRequest(anyLong(), anyLong());
    }

    @Test
    public void testShouldThrowIfSkillNotExist() {
        RecommendationRequestDto dto = createValidDto();
        RecommendationRequest existingRequest = new RecommendationRequest();
        existingRequest.setCreatedAt(LocalDateTime.now().minusMonths(6));

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(recommendationProperties.getCooldownMonths()).thenReturn(6);
        when(recommendationRequestRepository.findLatestPendingRequest(anyLong(), anyLong()))
                .thenReturn(Optional.of(existingRequest));
        when(skillRepository.existsByTitle(anyString())).thenReturn(false);

        assertThrows(SkillDoesntExistException.class, () -> validator.validate(dto));
        verify(skillRepository, times(1)).existsByTitle(anyString());
    }
}
