package com.abasilashvili.user_service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterDto {
    private String namePattern;
    private String phonePattern;
    private int experienceMin;
    private int experienceMax;
}
