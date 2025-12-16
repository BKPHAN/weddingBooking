package com.demo.web.repository;

import com.demo.web.model.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HallRepository extends JpaRepository<Hall, Long> {
    Optional<Hall> findByCode(String code);

    java.util.List<Hall> findByActiveTrue();
}
