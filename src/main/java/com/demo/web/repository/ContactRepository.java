package com.demo.web.repository;

import com.demo.web.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findTop10ByOrderByCreatedAtDesc();

    Optional<Contact> findTopByEmailIgnoreCaseOrderByCreatedAtDesc(String email);

    long countByFlagIn(Collection<String> flags);
}
