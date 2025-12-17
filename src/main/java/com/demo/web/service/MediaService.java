package com.demo.web.service;

import com.demo.web.model.MediaAlbum;
import com.demo.web.model.MediaItem;
import com.demo.web.repository.MediaAlbumRepository;
import com.demo.web.repository.MediaItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MediaService {

    private final MediaAlbumRepository albumRepository;
    private final MediaItemRepository itemRepository;

    public MediaService(MediaAlbumRepository albumRepository, MediaItemRepository itemRepository) {
        this.albumRepository = albumRepository;
        this.itemRepository = itemRepository;
    }

    public List<MediaAlbum> findAllAlbums() {
        return albumRepository.findAll();
    }

    public MediaAlbum findAlbumById(Long id) {
        return albumRepository.findById(id).orElse(null);
    }

    @Transactional
    public MediaAlbum saveAlbum(MediaAlbum album) {
        return albumRepository.save(album);
    }

    @Transactional
    public void deleteAlbum(Long id) {
        albumRepository.deleteById(id);
    }

    public List<MediaItem> findAllSliders() {
        return itemRepository.findByTypeOrderByDisplayOrderAsc(com.demo.web.model.enums.MediaType.SLIDER);
    }

    public List<MediaItem> findAllByType(com.demo.web.model.enums.MediaType type) {
        return itemRepository.findByTypeOrderByDisplayOrderAsc(type);
    }

    @Transactional
    public MediaItem saveMediaItem(MediaItem item) {
        return itemRepository.save(item);
    }

    @Transactional
    public void deleteMediaItem(Long id) {
        itemRepository.deleteById(id);
    }
}
