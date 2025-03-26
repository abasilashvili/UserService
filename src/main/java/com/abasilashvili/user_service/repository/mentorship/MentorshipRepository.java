package com.abasilashvili.user_service.repository.mentorship;

import org.springframework.data.repository.CrudRepository;
import com.abasilashvili.user_service.entity.User;

public interface MentorshipRepository extends CrudRepository<User, Long> {
}
