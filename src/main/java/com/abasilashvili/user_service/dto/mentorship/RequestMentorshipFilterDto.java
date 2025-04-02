package com.abasilashvili.user_service.dto.mentorship;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class RequestMentorshipFilterDto {
    @Pattern(
            regexp = "^(PENDING|ACCEPTED|REJECTED)$",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid status. Allowed values: ACCEPTED, PENDING, REJECTED"
    )
    private String status;
    private String rejectionReason;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAfter;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdBefore;
}
