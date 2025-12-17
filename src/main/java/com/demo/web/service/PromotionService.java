package com.demo.web.service;

import com.demo.web.model.Promotion;
import com.demo.web.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<Promotion> findAll() {
        return promotionRepository.findAll();
    }

    public Promotion findById(Long id) {
        return promotionRepository.findById(id).orElse(null);
    }

    @Transactional
    public Promotion savePromotion(Promotion promotion) {
        // Simple slug generation if empty
        if (promotion.getSlug() == null || promotion.getSlug().isEmpty()) {
            promotion.setSlug(toSlug(promotion.getTitle()));
        }
        return promotionRepository.save(promotion);
    }

    @Transactional
    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    private String toSlug(String input) {
        if (input == null)
            return "";
        return input.toLowerCase().replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
    }
}
