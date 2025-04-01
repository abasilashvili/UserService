package com.abasilashvili.user_service.controller.recommendation;

import com.abasilashvili.user_service.dto.recommendation.RecommendationRequestDto;
import com.abasilashvili.user_service.dto.recommendation.RejectionDto;
import com.abasilashvili.user_service.dto.recommendation.RequestFilterDto;
import com.abasilashvili.user_service.service.recommendation.RecommendationRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recrequest")
@RequiredArgsConstructor
@Slf4j
public class RecommendationRequestController {

    private final RecommendationRequestService recommendationRequestService;

    @PostMapping("/request")
    public RecommendationRequestDto requestRecommendation(@Valid @RequestBody RecommendationRequestDto requestDto) {
        log.info("Received request in Controller -requestRecommendation-");
        return recommendationRequestService.create(requestDto);
    }

    @GetMapping("/get-requests-with-filter")
    public List<RecommendationRequestDto> filterRecommendations(@Valid @RequestBody RequestFilterDto filterDto) {
        log.info("Received a request to filter recommendations. Controller");
        return recommendationRequestService.getFilteredRequests(filterDto);
    }

    @GetMapping("/get-request-by-id/{id}")
    public RecommendationRequestDto getRecommendationById(@PathVariable Long id) {
        log.info("Received a request to find user with id {}. Controller", id);
        return recommendationRequestService.getRequestById(id);
    }

    @PostMapping("/reject-request/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> rejectRequest(@PathVariable Long id, @RequestBody @Valid RejectionDto rejection) {
        log.info("Received request to reject pending recommendation. Controller");
        return recommendationRequestService.rejectRequest(id, rejection);
    }
}
