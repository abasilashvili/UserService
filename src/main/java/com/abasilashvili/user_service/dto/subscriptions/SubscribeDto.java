package com.abasilashvili.user_service.dto.subscriptions;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SubscribeDto {

    @Positive(message = "Follower Id must be positive number.")
    private Long followerId;
    @Positive(message = "Followee Id must be positive number.")
    private Long followeeId;
    private LocalDateTime subscribedAt;

}
