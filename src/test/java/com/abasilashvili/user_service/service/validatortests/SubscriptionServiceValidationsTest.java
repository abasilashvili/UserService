package com.abasilashvili.user_service.service.validatortests;

import com.abasilashvili.user_service.dto.subscriptions.SubscribeDto;
import com.abasilashvili.user_service.exceptions.SubscriptionAlreadyExistsException;
import com.abasilashvili.user_service.exceptions.ValidationException;
import com.abasilashvili.user_service.repository.SubscriptionRepository;
import com.abasilashvili.user_service.validator.subscriptions.SubscriptionServiceValidations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceValidationsTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionServiceValidations validations;

    @Test
    public void testValidationErrorWhenSameUser() {
        SubscribeDto dto = new SubscribeDto(1L, 1L, LocalDateTime.now());
        assertThrows(ValidationException.class, () -> validations.validateId(dto));
    }

    @Test
    public void testValidationExistsByFollowerId() {
        SubscribeDto dto = new SubscribeDto(1L, 2L, LocalDateTime.now());

        when(subscriptionRepository.existsByFollowerIdAndFolloweeId(anyLong(), anyLong()))
                .thenReturn(true);

        assertThrows(SubscriptionAlreadyExistsException.class,
                () -> validations.validateId(dto));
    }

    @Test
    public void testShouldPassWhenValid() {
        SubscribeDto dto = new SubscribeDto(1L, 2L, LocalDateTime.now());

        when(subscriptionRepository.existsByFollowerIdAndFolloweeId(anyLong(), anyLong()))
                .thenReturn(false);

        assertDoesNotThrow(() -> validations.validateId(dto));
    }
}
