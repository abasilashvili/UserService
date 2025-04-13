package com.abasilashvili.user_service.repository.contact;

import org.springframework.data.repository.CrudRepository;
import com.abasilashvili.user_service.entity.contact.Contact;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
}
