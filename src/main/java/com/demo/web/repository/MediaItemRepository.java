package com.demo.web.repository;

import com.demo.web.model.MediaItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaItemRepository extends JpaRepository<MediaItem, Long> {
    List<MediaItem> findByAlbumIdOrderByDisplayOrderAsc(Long albumId);

    void deleteByAlbumId(Long albumId);
}
