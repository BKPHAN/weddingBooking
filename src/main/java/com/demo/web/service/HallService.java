package com.demo.web.service;

import com.demo.web.model.Hall;
import com.demo.web.repository.HallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HallService {

    private final HallRepository hallRepository;

    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    @Transactional(readOnly = true)
    public List<Hall> findActiveHalls() {
        return hallRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<Hall> findFeaturedHalls() {
        // For now, just return top 3 active halls
        // In verify stage, we might want to add a 'featured' flag logic if needed
        List<Hall> active = hallRepository.findByActiveTrue();
        return active.size() > 3 ? active.subList(0, 3) : active;
    }
}
