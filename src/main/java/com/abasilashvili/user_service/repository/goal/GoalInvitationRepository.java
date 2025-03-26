package com.abasilashvili.user_service.repository.goal;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abasilashvili.user_service.entity.goal.GoalInvitation;

public interface GoalInvitationRepository extends JpaRepository<GoalInvitation, Long> {
}