package com.demo.web.controller.admin;

import com.demo.web.dto.ApiResponse;
import com.demo.web.dto.admin.MenuRequest;
import com.demo.web.dto.admin.MenuResponse;
import com.demo.web.mapper.admin.MenuAdminMapper;
import com.demo.web.service.admin.MenuAdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/menus")
@PreAuthorize("hasRole('ADMIN')")
public class MenuAdminController {

    private final MenuAdminService menuAdminService;
    private final MenuAdminMapper menuAdminMapper;

    public MenuAdminController(MenuAdminService menuAdminService,
                               MenuAdminMapper menuAdminMapper) {
        this.menuAdminService = menuAdminService;
        this.menuAdminMapper = menuAdminMapper;
    }

    @GetMapping
    public ApiResponse<List<MenuResponse>> list() {
        return new ApiResponse<>(
                true,
                "Danh sách menu",
                menuAdminMapper.toResponseList(menuAdminService.findAll())
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MenuResponse>> create(@Valid @RequestBody MenuRequest request) {
        var saved = menuAdminService.create(menuAdminMapper.toEntity(request));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Tạo menu thành công",
                menuAdminMapper.toResponse(saved)
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MenuResponse>> update(@PathVariable Long id,
                                                            @Valid @RequestBody MenuRequest request) {
        var updated = menuAdminService.update(id, menuAdminMapper.toEntity(request));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Cập nhật menu thành công",
                menuAdminMapper.toResponse(updated)
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        menuAdminService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Xoá menu thành công", null));
    }
}
