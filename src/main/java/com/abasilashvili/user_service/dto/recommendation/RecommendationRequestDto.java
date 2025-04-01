package com.abasilashvili.user_service.dto.recommendation;

import com.abasilashvili.user_service.dto.skill.SkillDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record RecommendationRequestDto(
        @Nullable long id,
        @NotBlank(message = "Message cannot be blank.") String message,
        @NotEmpty(message = "You need to request at least one skill.") List<SkillDto> skills,
        String status,
        @NotNull(message = "Incorrect ID") long requesterId,
        @NotNull(message = "Incorrect ID") long receiverId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}