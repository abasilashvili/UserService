package com.abasilashvili.user_service.controller;

import com.abasilashvili.user_service.dto.recommendation.RecommendationDto;
import com.abasilashvili.user_service.service.RecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/give-recommendation")
    public void giveRecommendation(@RequestBody @Valid RecommendationDto recommendation) {
        log.info("Получил запрос на создание рекоммендации.");
        recommendationService.giveRecommendation(recommendation);
    }

    @PutMapping("/update-recommendation")
    public void updateRecommendation(RecommendationDto updated) {
        log.info("Получил запрос на обновление рекоммендации.");
        recommendationService.updateRecommendation(updated);
    }

    @DeleteMapping("/delete-recommendation/{recId}")
    public void deleteRecommendation(@PathVariable Long recId) {
        log.info("Получил запрос на удаление рекоммендации.");
        recommendationService.deleteRecommendation(recId);
    }

    @GetMapping("/get-all-by-receiver/{receiverId}")
    public List<RecommendationDto> getAllUserRecommendation(@PathVariable Long receiverId) {
        log.info("Достать все рекоммендации пользователя.");
        return recommendationService.getAllUserRecommendation(receiverId);
    }

    @GetMapping("/get-all-by-author/{authorId}")
    public List<RecommendationDto> findAllByAuthorId(@PathVariable Long authorId) {
        log.info("Все рекоммендации автора с id {}", authorId);
        return recommendationService.findAllByAuthorId(authorId);
    }
}
