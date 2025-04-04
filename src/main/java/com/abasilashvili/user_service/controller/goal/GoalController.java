package com.abasilashvili.user_service.controller.goal;

import com.abasilashvili.user_service.dto.goal.GoalDto;
import com.abasilashvili.user_service.service.goal.GoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goal")
@RequiredArgsConstructor
@Slf4j
public class GoalController {

    private final GoalService goalService;

    @PostMapping("/{userId}/create")
    public GoalDto createGoal(@PathVariable Long userId, @RequestBody GoalDto dto) {
        log.info("Request to create a new goal.");
        return goalService.createGoal(userId, dto);
    }
}
