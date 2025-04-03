package com.abasilashvili.user_service.service.skills;

import com.abasilashvili.user_service.dto.skill.SkillCandidateDto;
import com.abasilashvili.user_service.dto.skill.SkillDto;
import com.abasilashvili.user_service.entity.Skill;
import com.abasilashvili.user_service.entity.recommendation.SkillOffer;
import com.abasilashvili.user_service.exceptions.AlreadyExistsException;
import com.abasilashvili.user_service.exceptions.NotEnoughOffersException;
import com.abasilashvili.user_service.mappers.skill.SkillCandidateDtoMapper;
import com.abasilashvili.user_service.mappers.skill.SkillDtoMapper;
import com.abasilashvili.user_service.repository.SkillRepository;
import com.abasilashvili.user_service.repository.recommendation.SkillOfferRepository;
import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillOfferRepository skillOfferRepository;
    private final SkillDtoMapper skillDtoMapper;
    private final SkillCandidateDtoMapper skillCandidateDtoMapper;

    public SkillDto createSkill(SkillDto skillDto) {
        log.info("Creating new Skill");
        Skill skill = skillDtoMapper.toEntity(skillDto);
        try {
            skill = skillRepository.save(skill);
        } catch (DataIntegrityViolationException e) {
            log.warn("Such skill already exists in db.");
            throw new AlreadyExistsException("Такой скилл уже существует. ");
        }
        log.info("Skill saved to database : {}", skill.getTitle());
        return skillDtoMapper.toDto(skill);
    }

    public List<SkillDto> getUserSkills(long userId) {
        log.info("Received a request to get all user skills by user-id");
        List<Skill> skills = skillRepository.findAllByUserId(userId);
        return skills.stream()
                .map(skillDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<SkillCandidateDto> getOfferedSkills(long userId) {
        log.info("Received a request to get offered skills by user id");
        return skillRepository.findSkillsOfferedToUser(userId).stream()
                .map(skillDtoMapper::toDto)
                .map(skillCandidateDtoMapper::toCandidate)
                .toList();
    }

    public SkillDto acquireSkillFromOffers(long skillId, long userId) {
        if (skillRepository.findUserSkill(skillId, userId).isPresent()) {
            throw new AlreadyExistsException("Такой скилл у пользователя уже есть.");
        }
        List<SkillOffer> offers = skillOfferRepository.findAllOffersOfSkill(skillId, userId);

        if (offers.size() < 3) {
            throw new NotEnoughOffersException("Недостаточно предложений для подтверждения скилла. Требуется минимум 3.");
        }

        skillRepository.assignSkillToUser(skillId, userId);

        return skillRepository.findById(skillId)
                .map(skillDtoMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Скилл не найден после назначения"));
    }
}
