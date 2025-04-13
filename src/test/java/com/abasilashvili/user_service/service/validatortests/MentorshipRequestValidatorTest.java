package com.abasilashvili.user_service.service.validatortests;

import com.abasilashvili.user_service.entity.MentorshipRequest;
import com.abasilashvili.user_service.exceptions.CooldownPeriodException;
import com.abasilashvili.user_service.exceptions.NoSuchMentorshipRequest;
import com.abasilashvili.user_service.exceptions.UserNotFoundException;
import com.abasilashvili.user_service.repository.UserRepository;
import com.abasilashvili.user_service.repository.mentorship.MentorshipRequestRepository;
import com.abasilashvili.user_service.validator.mentorship.MentorshipRequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MentorshipRequestValidatorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MentorshipRequestRepository mentorshipRequestRepository;

    @InjectMocks
    private MentorshipRequestValidator validator;

    @Test
    public void testShouldThrowUserNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> validator.validate(1L, 2L));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testShouldPassWhenNoPreviousRequest() {
        when(mentorshipRequestRepository.findLatestRequest(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validator.validateThreeMonthCooldown(1L, 2L));
    }

    @Test
    public void testShouldThrowWhenCooldownNotPassed() {
        MentorshipRequest mockRequest = mock(MentorshipRequest.class);
        when(mockRequest.getCreatedAt()).thenReturn(LocalDateTime.now().minusMonths(2));

        when(mentorshipRequestRepository.findLatestRequest(anyLong(), anyLong()))
                .thenReturn(Optional.of(mockRequest));

        assertThrows(CooldownPeriodException.class,
                () -> validator.validateThreeMonthCooldown(1L, 2L));
    }

    @Test
    public void testShouldPassWhenCooldownPassed() {
        MentorshipRequest mockRequest = mock(MentorshipRequest.class);

        when(mockRequest.getCreatedAt()).thenReturn(LocalDateTime.now().minusMonths(3));

        when(mentorshipRequestRepository.findLatestRequest(anyLong(), anyLong()))
                .thenReturn(Optional.of(mockRequest));

        assertDoesNotThrow(() -> validator.validateThreeMonthCooldown(1L, 2L));
    }

    @Test
    public void testShouldThrowWhenRequestDoesNotExist() {
        when(mentorshipRequestRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchMentorshipRequest.class,
                () -> validator.validateRequestExistence(1L));

        verify(mentorshipRequestRepository, times(1))
                .findById(1L);
    }

    @Test
    public void testShouldPassWhenRequestExists() {
        MentorshipRequest mockRequest = mock(MentorshipRequest.class);

        when(mentorshipRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(mockRequest));

        assertDoesNotThrow(() -> validator.validateRequestExistence(1L));
        verify(mentorshipRequestRepository, times(1))
                .findById(1L);
    }
}
