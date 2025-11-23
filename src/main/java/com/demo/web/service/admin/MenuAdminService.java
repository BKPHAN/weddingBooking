package com.demo.web.service.admin;

import com.demo.web.model.Menu;
import com.demo.web.repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuAdminService {

    private final MenuRepository menuRepository;

    public MenuAdminService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Transactional(readOnly = true)
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Transactional
    public Menu create(Menu menu) {
        return menuRepository.save(menu);
    }

    @Transactional
    public Menu update(Long id, Menu incoming) {
        Menu existing = menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found: " + id));

        existing.setName(incoming.getName());
        existing.setPrice(incoming.getPrice());
        existing.setDescription(incoming.getDescription());
        existing.setCategory(incoming.getCategory());
        existing.setImageUrl(incoming.getImageUrl());
        existing.setFeatured(incoming.getFeatured());

        return menuRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!menuRepository.existsById(id)) {
            throw new EntityNotFoundException("Menu not found: " + id);
        }
        menuRepository.deleteById(id);
    }
}
