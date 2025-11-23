package com.demo.web.repository;

import com.demo.web.model.MediaAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaAlbumRepository extends JpaRepository<MediaAlbum, Long> {
}
