package com.abasilashvili.user_service.service.subscriptions;

import com.abasilashvili.user_service.dto.subscriptions.SubscribeDto;
import com.abasilashvili.user_service.dto.user.UserDto;
import com.abasilashvili.user_service.dto.user.UserFilterDto;
import com.abasilashvili.user_service.exceptions.SubscriptionAlreadyExistsException;
import com.abasilashvili.user_service.exceptions.ValidationException;
import com.abasilashvili.user_service.mappers.user.UserMapper;
import com.abasilashvili.user_service.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class SubscriptionService {

    private final SubscriptionServiceValidations validator;
    private final SubscriptionRepository subscriptionRepository;
    private final UserMapper userMapper;

    public ResponseEntity<String> followUser(SubscribeDto dto) {
        validator.validateId(dto);
        log.info("Received request to follow user. Service. Validation passed.");
        subscriptionRepository.followUser(dto.getFollowerId(), dto.getFolloweeId());

        return ResponseEntity.ok("You are now subscribed to this user!");
    }

    public ResponseEntity<String> unfollowUser(SubscribeDto dto) {
        validator.validateId(dto);
        log.info("Received request to unfollow user. Service. Validation passed.");
        subscriptionRepository.unfollowUser(dto.getFollowerId(), dto.getFolloweeId());

        return ResponseEntity.ok("You unfollowed user!");
    }

    public List<UserDto> getFollowers(Long id, UserFilterDto filter) {
        log.info("Request to provide with followers list with filters. Service.");
        return subscriptionRepository.findByFolloweeId(id)
                .map(userMapper::toDto)
                .filter(userDto -> matchesFilter(userDto, filter))
                .toList();
    }

    public int getFollowersCount(Long userId) {
        log.info("Receive followers count for user with id : {}", userId);
        return subscriptionRepository.findFollowersAmountByFolloweeId(userId);
    }

    public List<UserDto> getFollowing(Long userId, UserFilterDto dto) {
        log.info("Get followers list with filters.");
        return subscriptionRepository.findByFollowerId(userId)
                .map(userMapper::toDto)
                .filter(userDto -> matchesFilter(userDto, dto))
                .toList();
    }

    public int getFollowingCount(Long userId) {
        log.info("Get users follower count.");
        return subscriptionRepository.findFolloweesAmountByFollowerId(userId);
    }

    private boolean matchesFilter(UserDto userDto, UserFilterDto filter) {
        return matchesName(userDto, filter.getNamePattern()) &&
                matchesPhone(userDto, filter.getPhonePattern()) &&
                matchesExperience(userDto, filter.getExperienceMin(), filter.getExperienceMax());
    }

    private boolean matchesName(UserDto user, String pattern) {
        return pattern == null || pattern.isEmpty() ||
                user.getUsername().toLowerCase().contains(pattern.toLowerCase());
    }

    private boolean matchesPhone(UserDto user, String pattern) {
        return pattern == null || pattern.isEmpty() ||
                (user.getPhone() != null && user.getPhone().contains(pattern));
    }

    private boolean matchesExperience(UserDto user, int min, int max) {
        return user.getExperience() >= min && user.getExperience() <= max;
    }
}

@Component
@Slf4j
@RequiredArgsConstructor
class SubscriptionServiceValidations {

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