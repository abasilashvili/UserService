package com.abasilashvili.user_service.validator.subscriptions;

import com.abasilashvili.user_service.dto.subscriptions.SubscribeDto;
import com.abasilashvili.user_service.exceptions.SubscriptionAlreadyExistsException;
import com.abasilashvili.user_service.exceptions.ValidationException;
import com.abasilashvili.user_service.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class SubscriptionServiceValidations {

    private final SubscriptionRepository subscriptionRepository;

    public void validateId(SubscribeDto dto) {

        log.info("Validating request.");

        Long followerId = dto.getFollowerId();
        Long followeeId = dto.getFolloweeId();

        if (Objects.equals(followeeId, followerId)) {
            throw new ValidationException("You can't subscribe/unsubscribe yourself!");
        }

        if (subscriptionRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            throw new SubscriptionAlreadyExistsException("You are already following this user.");
        }
    }
}
