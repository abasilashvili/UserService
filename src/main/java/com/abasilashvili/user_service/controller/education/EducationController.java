package com.abasilashvili.user_service.controller.education;

import com.abasilashvili.user_service.dto.education.EducationDto;
import com.abasilashvili.user_service.service.education.EducationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/education")
@RequiredArgsConstructor
@Slf4j
public class EducationController {

    private final EducationService educationService;

    @PostMapping("/{userId}/add")
    public EducationDto addEducation(@PathVariable Long userId, @RequestBody EducationDto educationDto) {
        log.info("Request to add education for user with id : {}", userId);
        return educationService.addEducation(userId, educationDto);
    }

    @PutMapping("/{userId}/update")
    public EducationDto updateEducation(@PathVariable Long userId, @RequestBody EducationDto educationDto) {
        log.info("Request to update education for user with id : {}", userId);
        return educationService.updateEducation(userId, educationDto);
    }

    @GetMapping("/{educationId}/getEdu")
    public EducationDto getEducationById(@PathVariable Long eduId) {
        log.info("Request to get Education with id : {}", eduId);
        return educationService.getEducationById(eduId);
    }
}
