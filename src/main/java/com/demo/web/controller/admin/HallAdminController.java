package com.demo.web.controller.admin;

import com.demo.web.dto.ApiResponse;
import com.demo.web.dto.admin.HallRequest;
import com.demo.web.dto.admin.HallResponse;
import com.demo.web.mapper.admin.HallAdminMapper;
import com.demo.web.service.admin.HallAdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/halls")
@PreAuthorize("hasRole('ADMIN')")
public class HallAdminController {

    private final HallAdminService hallAdminService;
    private final HallAdminMapper hallAdminMapper;

    public HallAdminController(HallAdminService hallAdminService,
                               HallAdminMapper hallAdminMapper) {
        this.hallAdminService = hallAdminService;
        this.hallAdminMapper = hallAdminMapper;
    }

    @GetMapping
    public ApiResponse<List<HallResponse>> list() {
        return new ApiResponse<>(
                true,
                "Danh sách sảnh",
                hallAdminMapper.toResponseList(hallAdminService.findAll())
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HallResponse>> create(@Valid @RequestBody HallRequest request) {
        var saved = hallAdminService.create(hallAdminMapper.toEntity(request));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Tạo sảnh thành công",
                hallAdminMapper.toResponse(saved)
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<HallResponse>> update(@PathVariable Long id,
                                                            @Valid @RequestBody HallRequest request) {
        var updated = hallAdminService.update(id, hallAdminMapper.toEntity(request));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Cập nhật sảnh thành công",
                hallAdminMapper.toResponse(updated)
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        hallAdminService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Xoá sảnh thành công", null));
    }
}
