package com.abasilashvili.user_service.dto.user;

import com.abasilashvili.user_service.dto.mentorship.MenteeDto;
import com.abasilashvili.user_service.dto.mentorship.MentorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private int experience;
    private List<MentorDto> mentors;
    private List<MenteeDto> mentees;
}
