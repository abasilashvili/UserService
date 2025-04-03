package com.abasilashvili.user_service.controller.subscriptions;

import com.abasilashvili.user_service.dto.subscriptions.SubscribeDto;
import com.abasilashvili.user_service.dto.user.UserDto;
import com.abasilashvili.user_service.dto.user.UserFilterDto;
import com.abasilashvili.user_service.service.subscriptions.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PutMapping("/subscribe-to")
    public ResponseEntity<String> followUser(@RequestBody @Valid SubscribeDto dto) {
        log.info("Follow user request. Controller");
        return subscriptionService.followUser(dto);
    }

    @PutMapping("/unsubscribe-from")
    public ResponseEntity<String> unfollowUser(@RequestBody @Valid SubscribeDto dto) {
        log.info("Unfollow user request. Controller");
        return subscriptionService.unfollowUser(dto);
    }

    @GetMapping("/{followeeId}/followers")
    public List<UserDto> getFollowers(@PathVariable Long followeeId, @RequestBody UserFilterDto dto) {
        log.info("Return followers list with filters.");
        return subscriptionService.getFollowers(followeeId, dto);
    }

    @GetMapping("/get-follower-count/{userId}")
    public int getFollowersCount(@PathVariable Long userId) {
        log.info("Get followers count.");
        return subscriptionService.getFollowersCount(userId);
    }

    @GetMapping("/{userId}/followers")
    public List<UserDto> getFollowing(@PathVariable Long userId, @RequestBody UserFilterDto dto) {
        log.info("Get following users with filters.");
        return subscriptionService.getFollowing(userId, dto);
    }

    @GetMapping("/get-following-count/{userId}")
    public int getFollowingCount(@PathVariable Long userId) {
        log.info("Get requesters count of followers.");
        return subscriptionService.getFollowingCount(userId);
    }
}
