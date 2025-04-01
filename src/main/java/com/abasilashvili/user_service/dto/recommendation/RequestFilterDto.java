package com.abasilashvili.user_service.dto.recommendation;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RequestFilterDto {

    @Positive(message = "User ID must be a positive number.")
    private Long userId;
    @Pattern(
            regexp = "^(PENDING|ACCEPTED|REJECTED)$",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid status. Allowed values: ACCEPTED, PENDING, REJECTED"
    )
    private String status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime afterDate;
}
