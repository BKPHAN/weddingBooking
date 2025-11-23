package com.demo.web.controller.admin;

import com.demo.web.dto.ApiResponse;
import com.demo.web.dto.admin.MediaItemRequest;
import com.demo.web.dto.admin.MediaItemResponse;
import com.demo.web.mapper.admin.MediaItemAdminMapper;
import com.demo.web.service.admin.MediaItemAdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/media/albums/{albumId}/items")
@PreAuthorize("hasRole('ADMIN')")
public class MediaItemAdminController {

    private final MediaItemAdminService mediaItemAdminService;
    private final MediaItemAdminMapper mediaItemAdminMapper;

    public MediaItemAdminController(MediaItemAdminService mediaItemAdminService,
                                    MediaItemAdminMapper mediaItemAdminMapper) {
        this.mediaItemAdminService = mediaItemAdminService;
        this.mediaItemAdminMapper = mediaItemAdminMapper;
    }

    @GetMapping
    public ApiResponse<List<MediaItemResponse>> list(@PathVariable Long albumId) {
        return new ApiResponse<>(
                true,
                "Danh sách media",
                mediaItemAdminMapper.toResponseList(mediaItemAdminService.listByAlbum(albumId))
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MediaItemResponse>> create(@PathVariable Long albumId,
                                                                 @Valid @RequestBody MediaItemRequest request) {
        var saved = mediaItemAdminService.create(albumId, mediaItemAdminMapper.toEntity(request));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Thêm media thành công",
                mediaItemAdminMapper.toResponse(saved)
        ));
    }

    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MediaItemResponse>> update(@PathVariable Long albumId,
                                                                 @PathVariable Long itemId,
                                                                 @Valid @RequestBody MediaItemRequest request) {
        var updated = mediaItemAdminService.update(albumId, itemId, mediaItemAdminMapper.toEntity(request));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Cập nhật media thành công",
                mediaItemAdminMapper.toResponse(updated)
        ));
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long albumId,
                                                    @PathVariable Long itemId) {
        mediaItemAdminService.delete(albumId, itemId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Xoá media thành công", null));
    }
}
