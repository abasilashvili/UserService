package com.abasilashvili.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abasilashvili.user_service.entity.ContentData;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentDataRepository extends JpaRepository<ContentData, Long> {
}
