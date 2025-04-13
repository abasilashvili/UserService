package com.abasilashvili.user_service.repository.contact;

import org.springframework.data.repository.CrudRepository;
import com.abasilashvili.user_service.entity.contact.ContactPreference;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactPreferenceRepository extends CrudRepository<ContactPreference, Long> {
}
