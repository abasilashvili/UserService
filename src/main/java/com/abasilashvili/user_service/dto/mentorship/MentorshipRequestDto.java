package com.abasilashvili.user_service.dto.mentorship;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MentorshipRequestDto {
    private Long id;
    private String description;
    private MenteeDto requester;
    private MentorDto receiver;
    private LocalDateTime createdAt;
    private String motivationLetter;
}
