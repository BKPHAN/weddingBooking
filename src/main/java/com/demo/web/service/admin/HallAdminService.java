package com.demo.web.service.admin;

import com.demo.web.model.Hall;
import com.demo.web.repository.HallRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HallAdminService {

    private final HallRepository hallRepository;

    public HallAdminService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    @Transactional(readOnly = true)
    public List<Hall> findAll() {
        return hallRepository.findAll();
    }

    @Transactional
    public Hall create(Hall hall) {
        return hallRepository.save(hall);
    }

    @Transactional
    public Hall update(Long id, Hall incoming) {
        Hall existing = hallRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hall not found: " + id));

        existing.setCode(incoming.getCode());
        existing.setName(incoming.getName());
        existing.setCapacity(incoming.getCapacity());
        existing.setBasePrice(incoming.getBasePrice());
        existing.setDescription(incoming.getDescription());
        existing.setAmenities(incoming.getAmenities());
        existing.setImageUrl(incoming.getImageUrl());
        existing.setDisplayOrder(incoming.getDisplayOrder());
        existing.setActive(incoming.getActive());

        return hallRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!hallRepository.existsById(id)) {
            throw new EntityNotFoundException("Hall not found: " + id);
        }
        hallRepository.deleteById(id);
    }
}
