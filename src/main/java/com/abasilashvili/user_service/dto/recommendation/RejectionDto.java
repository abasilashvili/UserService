package com.abasilashvili.user_service.dto.recommendation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RejectionDto {
    @Positive(message = "User Id should be positive number.")
    private Long userId;
    @NotEmpty(message = "You need to provide reason for rejection.")
    private String reason;
}
