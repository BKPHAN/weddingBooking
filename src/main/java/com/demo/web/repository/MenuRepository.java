package com.demo.web.repository;

import com.demo.web.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByCategory(String category);

    List<Menu> findByType(String type);
}
