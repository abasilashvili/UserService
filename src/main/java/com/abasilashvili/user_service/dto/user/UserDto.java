package com.abasilashvili.user_service.dto.user;

import com.abasilashvili.user_service.dto.mentorship.MenteeDto;
import com.abasilashvili.user_service.dto.mentorship.MentorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private List<MentorDto> mentors;
    private List<MenteeDto> mentees;
}
