package com.demo.web.service.admin;

import com.demo.web.model.Promotion;
import com.demo.web.repository.PromotionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PromotionAdminService {

    private final PromotionRepository promotionRepository;

    public PromotionAdminService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Transactional(readOnly = true)
    public List<Promotion> findAll() {
        return promotionRepository.findAll();
    }

    @Transactional
    public Promotion create(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    @Transactional
    public Promotion update(Long id, Promotion incoming) {
        Promotion existing = promotionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found: " + id));

        existing.setTitle(incoming.getTitle());
        existing.setSlug(incoming.getSlug());
        existing.setDescription(incoming.getDescription());
        existing.setStartDate(incoming.getStartDate());
        existing.setEndDate(incoming.getEndDate());
        existing.setTerms(incoming.getTerms());

        return promotionRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!promotionRepository.existsById(id)) {
            throw new EntityNotFoundException("Promotion not found: " + id);
        }
        promotionRepository.deleteById(id);
    }
}
