package com.abasilashvili.user_service.service.servicetests;

import com.abasilashvili.user_service.dto.recommendation.RecommendationRequestDto;
import com.abasilashvili.user_service.dto.skill.SkillDto;
import com.abasilashvili.user_service.entity.RequestStatus;
import com.abasilashvili.user_service.entity.Skill;
import com.abasilashvili.user_service.entity.User;
import com.abasilashvili.user_service.entity.recommendation.RecommendationRequest;
import com.abasilashvili.user_service.exceptions.ValidationException;
import com.abasilashvili.user_service.mappers.recommendation.RecommendationRequestMapper;
import com.abasilashvili.user_service.repository.SkillRepository;
import com.abasilashvili.user_service.repository.UserRepository;
import com.abasilashvili.user_service.repository.recommendation.RecommendationRequestRepository;
import com.abasilashvili.user_service.repository.recommendation.SkillRequestRepository;
import com.abasilashvili.user_service.service.recommendation.RecommendationRequestService;
import com.abasilashvili.user_service.validator.recommendations.RecommendationRequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecommendationRequestServiceTest {

    @Mock
    private RecommendationRequestRepository recommendationRequestRepository;

    @Mock
    private SkillRequestRepository skillRequestRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecommendationRequestValidator validator;

    @Mock
    private RecommendationRequestMapper recommendationRequestMapper;

    @InjectMocks
    private RecommendationRequestService recommendationRequestService;

    private RecommendationRequestDto createValidDto() {
        return new RecommendationRequestDto(1L, "Rec Message",
                List.of(new SkillDto(1L, "Java")), "pending",
                1L, 2L,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    public void create_ShouldThrowWithIncorrectUser() {
        RecommendationRequestDto validDto = createValidDto();

        when(userRepository.findById(validDto.requesterId()))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> recommendationRequestService.create(validDto));
    }

    @Test
    public void create_ShouldThrowIfSixMonthPeriodInvalid() {
        RecommendationRequestDto validDto = createValidDto();

        doThrow(new ValidationException("Request within six months is not allowed"))
                .when(validator).validate(validDto);

        assertThrows(ValidationException.class, () -> recommendationRequestService.create(validDto));
    }

    @Test
    public void create_ShouldThrowIfSkillDoesNotExist() {
        RecommendationRequestDto validDto = createValidDto();

        doThrow(new ValidationException("Skill does not exist"))
                .when(validator).validate(validDto);

        assertThrows(ValidationException.class, () -> recommendationRequestService.create(validDto));
    }

    @Test
    public void create_Success() {
        RecommendationRequestDto validDto = createValidDto();

        doNothing().when(validator).validate(validDto);

        User requester = mock(User.class);
        User receiver = mock(User.class);
        Skill skill = mock(Skill.class);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(requester));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(receiver));

        when(skillRepository.findById(any()))
                .thenReturn(Optional.of(skill));

        RecommendationRequest savedRequest = RecommendationRequest.builder()
                .requester(requester)
                .receiver(receiver)
                .message(validDto.message())
                .status(RequestStatus.PENDING)
                .skills(new ArrayList<>())
                .build();
        when(recommendationRequestRepository.save(any(RecommendationRequest.class)))
                .thenReturn(savedRequest);

        recommendationRequestService.create(validDto);

        RecommendationRequestDto result = recommendationRequestMapper.toDto(savedRequest);

                ArgumentCaptor<RecommendationRequest> requestCaptor =
                ArgumentCaptor.forClass(RecommendationRequest.class);
        verify(recommendationRequestRepository).save(requestCaptor.capture());

        RecommendationRequest capturedRequest = requestCaptor.getValue();

        assertEquals(capturedRequest.getRequester(), requester);

        verify(recommendationRequestRepository, times(1)).save(any());
    }
}
