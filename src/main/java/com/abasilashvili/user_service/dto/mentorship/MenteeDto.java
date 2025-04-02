package com.abasilashvili.user_service.dto.mentorship;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenteeDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
}
