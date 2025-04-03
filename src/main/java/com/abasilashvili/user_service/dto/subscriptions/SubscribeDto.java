package com.abasilashvili.user_service.dto.subscriptions;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscribeDto {

    @Positive(message = "Follower Id must be positive number.")
    private Long followerId;
    @Positive(message = "Followee Id must be positive number.")
    private Long followeeId;
    private LocalDateTime subscribedAt;

}
