package com.abasilashvili.user_service.repository.premium;

import org.springframework.data.repository.CrudRepository;
import com.abasilashvili.user_service.entity.premium.Premium;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PremiumRepository extends CrudRepository<Premium, Long> {

    boolean existsByUserId(long userId);

    List<Premium> findAllByEndDateBefore(LocalDateTime endDate);
}
