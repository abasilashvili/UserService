package com.abasilashvili.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abasilashvili.user_service.entity.Career;

public interface CareerRepository extends JpaRepository<Career, Long> {

}
