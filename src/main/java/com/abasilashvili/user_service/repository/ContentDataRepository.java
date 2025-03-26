package com.abasilashvili.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abasilashvili.user_service.entity.ContentData;

public interface ContentDataRepository extends JpaRepository<ContentData, Long> {
}
