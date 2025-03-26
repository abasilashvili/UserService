package com.abasilashvili.user_service.dto.skill;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkillDto {

    private Long id;

    @NotBlank(message = "Название навыка не может быть пустым.")
    private String title;
}
