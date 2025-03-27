package com.abasilashvili.user_service.service;

import com.abasilashvili.user_service.dto.recommendation.RecommendationDto;
import com.abasilashvili.user_service.dto.recommendation.SkillOfferDto;
import com.abasilashvili.user_service.exceptions.AlreadyExistsException;
import com.abasilashvili.user_service.exceptions.ValidationException;
import com.abasilashvili.user_service.mappers.RecommendationDtoMapper;
import com.abasilashvili.user_service.repository.recommendation.RecommendationRepository;
import com.abasilashvili.user_service.repository.recommendation.SkillOfferRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final SkillOfferRepository skillOfferRepository;
    private final RecommendationDtoMapper recommendationDtoMapper;

    @Transactional
    public void giveRecommendation(RecommendationDto recommendation) {
        validateRecommendation(recommendation);

        Long recommendationId = recommendationRepository.create(
                recommendation.getAuthorId(),
                recommendation.getReceiverId(),
                recommendation.getContent()
        );

        for (SkillOfferDto skillOffer : recommendation.getSkillOffers()) {
            skillOfferRepository.create(skillOffer.getId(), recommendationId);
        }

        log.info("Создал рекоммендацию со следующим ID {}", recommendationId);
    }

    @Transactional
    public void updateRecommendation(RecommendationDto updatedDto) {
        log.info("Обновление существующей записи с id {}", updatedDto.getId());
        validateRecommendation(updatedDto);

        recommendationRepository.update(
                updatedDto.getAuthorId(),
                updatedDto.getReceiverId(),
                updatedDto.getContent()
        );

        skillOfferRepository.deleteAllByRecommendationId(updatedDto.getId());

        List<SkillOfferDto> skillOfferDtos = updatedDto.getSkillOffers();
        for (SkillOfferDto skillOfferDto : skillOfferDtos) {
            skillOfferRepository.create(skillOfferDto.getSkillId(), updatedDto.getId());
        }
        log.info("Запись обновлена.");
    }

    public void deleteRecommendation(Long id) {
        log.info("Удаление рекоммендации с id {}", id);
        recommendationRepository.deleteById(id);
        log.info("Запись {} удалена.", id);
    }

    public List<RecommendationDto> getAllUserRecommendation(Long receiverId) {
        log.info("Запрос всех рекоммендаций по id {}", receiverId);
        return recommendationRepository.findAllByReceiverId(receiverId, Pageable.ofSize(Integer.MAX_VALUE)).stream()
                .map(recommendationDtoMapper::toDto)
                .toList();
    }

    public List<RecommendationDto> findAllByAuthorId(Long authorId) {
        log.info("Все рекоммендации автора с id {}", authorId);
        return recommendationRepository.findAllByAuthorId(authorId, Pageable.ofSize(Integer.MAX_VALUE)).stream()
                .map(recommendationDtoMapper::toDto)
                .toList();
    }

    private void validateRecommendation(RecommendationDto recommendationDto) {
        if (recommendationDto.getAuthorId() == null || recommendationDto.getContent().isEmpty()) {
            throw new ValidationException("Поле не может быть пустым");
        } else if (recommendationDto.getReceiverId() == null) {
            throw new ValidationException("Не указан получатель рекоммендации");
        }

        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        if (recommendationDto.getCreatedAt() != null &&
                recommendationDto.getCreatedAt().isAfter(sixMonthsAgo)) {
            throw new AlreadyExistsException("Не прошло 6 месяцев с последней рекоммендации.");
        }

        List<SkillOfferDto> skillOfferDtos = recommendationDto.getSkillOffers();
        for (SkillOfferDto skillOffer : skillOfferDtos) {
            if (skillOffer == null || skillOffer.getSkillId() == null) {
                throw new ValidationException("Поля не могут быть пустыми.");
            }
        }
    }
}
