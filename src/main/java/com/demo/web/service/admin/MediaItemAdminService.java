package com.demo.web.service.admin;

import com.demo.web.model.MediaAlbum;
import com.demo.web.model.MediaItem;
import com.demo.web.repository.MediaItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MediaItemAdminService {

    private final MediaItemRepository mediaItemRepository;
    private final MediaAlbumAdminService mediaAlbumAdminService;

    public MediaItemAdminService(MediaItemRepository mediaItemRepository,
                                 MediaAlbumAdminService mediaAlbumAdminService) {
        this.mediaItemRepository = mediaItemRepository;
        this.mediaAlbumAdminService = mediaAlbumAdminService;
    }

    @Transactional(readOnly = true)
    public List<MediaItem> listByAlbum(Long albumId) {
        return mediaItemRepository.findByAlbumIdOrderByDisplayOrderAsc(albumId);
    }

    @Transactional
    public MediaItem create(Long albumId, MediaItem item) {
        MediaAlbum album = mediaAlbumAdminService.getById(albumId);
        item.setAlbum(album);
        return mediaItemRepository.save(item);
    }

    @Transactional
    public MediaItem update(Long albumId, Long itemId, MediaItem incoming) {
        MediaItem existing = mediaItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Media item not found: " + itemId));
        if (!existing.getAlbum().getId().equals(albumId)) {
            throw new EntityNotFoundException("Media item not found in album " + albumId);
        }

        existing.setTitle(incoming.getTitle());
        existing.setType(incoming.getType());
        existing.setUrl(incoming.getUrl());
        existing.setThumbnailUrl(incoming.getThumbnailUrl());
        existing.setDisplayOrder(incoming.getDisplayOrder());

        return mediaItemRepository.save(existing);
    }

    @Transactional
    public void delete(Long albumId, Long itemId) {
        MediaItem existing = mediaItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Media item not found: " + itemId));
        if (!existing.getAlbum().getId().equals(albumId)) {
            throw new EntityNotFoundException("Media item not found in album " + albumId);
        }
        mediaItemRepository.delete(existing);
    }
}
