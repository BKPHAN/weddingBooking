package com.demo.web.controller.admin;

import com.demo.web.dto.ApiResponse;
import com.demo.web.dto.admin.MediaAlbumRequest;
import com.demo.web.dto.admin.MediaAlbumResponse;
import com.demo.web.mapper.admin.MediaAlbumAdminMapper;
import com.demo.web.service.admin.MediaAlbumAdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/media/albums")
@PreAuthorize("hasRole('ADMIN')")
public class MediaAlbumAdminController {

    private final MediaAlbumAdminService mediaAlbumAdminService;
    private final MediaAlbumAdminMapper mediaAlbumAdminMapper;

    public MediaAlbumAdminController(MediaAlbumAdminService mediaAlbumAdminService,
                                     MediaAlbumAdminMapper mediaAlbumAdminMapper) {
        this.mediaAlbumAdminService = mediaAlbumAdminService;
        this.mediaAlbumAdminMapper = mediaAlbumAdminMapper;
    }

    @GetMapping
    public ApiResponse<List<MediaAlbumResponse>> list() {
        return new ApiResponse<>(
                true,
                "Danh sách album",
                mediaAlbumAdminMapper.toResponseList(mediaAlbumAdminService.findAll())
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MediaAlbumResponse>> create(@Valid @RequestBody MediaAlbumRequest request) {
        var saved = mediaAlbumAdminService.create(mediaAlbumAdminMapper.toEntity(request));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Tạo album thành công",
                mediaAlbumAdminMapper.toResponse(saved)
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MediaAlbumResponse>> update(@PathVariable Long id,
                                                                  @Valid @RequestBody MediaAlbumRequest request) {
        var updated = mediaAlbumAdminService.update(id, mediaAlbumAdminMapper.toEntity(request));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Cập nhật album thành công",
                mediaAlbumAdminMapper.toResponse(updated)
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        mediaAlbumAdminService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Xoá album thành công", null));
    }
}
