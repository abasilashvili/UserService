package com.abasilashvili.user_service.repository;

import org.springframework.data.repository.CrudRepository;
import com.abasilashvili.user_service.entity.Country;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
}