package com.demo.web.service.admin;

import com.demo.web.model.MediaAlbum;
import com.demo.web.repository.MediaAlbumRepository;
import com.demo.web.repository.MediaItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MediaAlbumAdminService {

    private final MediaAlbumRepository mediaAlbumRepository;
    private final MediaItemRepository mediaItemRepository;

    public MediaAlbumAdminService(MediaAlbumRepository mediaAlbumRepository,
                                  MediaItemRepository mediaItemRepository) {
        this.mediaAlbumRepository = mediaAlbumRepository;
        this.mediaItemRepository = mediaItemRepository;
    }

    @Transactional(readOnly = true)
    public List<MediaAlbum> findAll() {
        return mediaAlbumRepository.findAll();
    }

    @Transactional
    public MediaAlbum create(MediaAlbum album) {
        return mediaAlbumRepository.save(album);
    }

    @Transactional
    public MediaAlbum update(Long id, MediaAlbum incoming) {
        MediaAlbum existing = mediaAlbumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Media album not found: " + id));
        existing.setTitle(incoming.getTitle());
        existing.setDescription(incoming.getDescription());
        return mediaAlbumRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        MediaAlbum album = mediaAlbumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Media album not found: " + id));

        mediaItemRepository.deleteByAlbumId(album.getId());
        mediaAlbumRepository.delete(album);
    }

    @Transactional(readOnly = true)
    public MediaAlbum getById(Long id) {
        return mediaAlbumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Media album not found: " + id));
    }
}
