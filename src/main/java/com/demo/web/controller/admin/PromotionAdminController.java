package com.demo.web.controller.admin;

import com.demo.web.dto.ApiResponse;
import com.demo.web.dto.admin.PromotionRequest;
import com.demo.web.dto.admin.PromotionResponse;
import com.demo.web.mapper.admin.PromotionAdminMapper;
import com.demo.web.service.admin.PromotionAdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/promotions")
@PreAuthorize("hasRole('ADMIN')")
public class PromotionAdminController {

    private final PromotionAdminService promotionAdminService;
    private final PromotionAdminMapper promotionAdminMapper;

    public PromotionAdminController(PromotionAdminService promotionAdminService,
                                    PromotionAdminMapper promotionAdminMapper) {
        this.promotionAdminService = promotionAdminService;
        this.promotionAdminMapper = promotionAdminMapper;
    }

    @GetMapping
    public ApiResponse<List<PromotionResponse>> list() {
        return new ApiResponse<>(
                true,
                "Danh sách khuyến mãi",
                promotionAdminMapper.toResponseList(promotionAdminService.findAll())
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PromotionResponse>> create(@Valid @RequestBody PromotionRequest request) {
        var saved = promotionAdminService.create(promotionAdminMapper.toEntity(request));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Tạo khuyến mãi thành công",
                promotionAdminMapper.toResponse(saved)
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PromotionResponse>> update(@PathVariable Long id,
                                                                 @Valid @RequestBody PromotionRequest request) {
        var updated = promotionAdminService.update(id, promotionAdminMapper.toEntity(request));
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Cập nhật khuyến mãi thành công",
                promotionAdminMapper.toResponse(updated)
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        promotionAdminService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Xoá khuyến mãi thành công", null));
    }
}
