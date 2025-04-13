package com.abasilashvili.user_service.repository;

import org.springframework.data.repository.CrudRepository;
import com.abasilashvili.user_service.entity.UserSkillGuarantee;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSkillGuaranteeRepository extends CrudRepository<UserSkillGuarantee, Long> {
}