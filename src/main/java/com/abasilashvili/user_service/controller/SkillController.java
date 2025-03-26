package com.abasilashvili.user_service.controller;

import com.abasilashvili.user_service.dto.skill.SkillCandidateDto;
import com.abasilashvili.user_service.dto.skill.SkillDto;
import com.abasilashvili.user_service.entity.Skill;
import com.abasilashvili.user_service.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/skills")
public class SkillController {

    private final SkillService skillService;

    @PostMapping("/create-skill")
    public SkillDto createSkill(@RequestBody @Valid SkillDto skillDto) {
        log.info("Received request to create skill in Controller");
        return skillService.createSkill(skillDto);
    }

    @GetMapping("/get-all-users-skills")
    public List<SkillDto> getUserSkills(long userId) {
        log.info("Received a request in Controller to get all user skills by user-id");
        return skillService.getUserSkills(userId);
    }

    public List<SkillCandidateDto> getOfferedSkills(long userId) {
        log.info("Received a request in Controller to get offered skills by user id");
        return skillService.getOfferedSkills(userId);
    }

    public SkillDto acquireSkillFromOffers(long skillId, long userId) {
        return null;
    }
}
