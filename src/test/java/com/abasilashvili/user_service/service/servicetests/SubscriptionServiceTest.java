package com.abasilashvili.user_service.service.servicetests;

import com.abasilashvili.user_service.dto.subscriptions.SubscribeDto;
import com.abasilashvili.user_service.mappers.user.UserMapper;
import com.abasilashvili.user_service.repository.SubscriptionRepository;
import com.abasilashvili.user_service.service.subscriptions.SubscriptionService;
import com.abasilashvili.user_service.validator.subscriptions.SubscriptionServiceValidations;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Spy
    private UserMapper userMapper;

    @Mock
    SubscriptionServiceValidations validations;

    @InjectMocks
    private SubscriptionService subscriptionService;

//    @BeforeEach
//    public void init() {
//        SubscribeDto dto = new SubscribeDto();
//        dto.setFolloweeId(1L);
//        dto.setFollowerId(1L);
//        dto.setSubscribedAt(LocalDateTime.now());
//    }

    @Test
    public void testValidator() {

    }
}
