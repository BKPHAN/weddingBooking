package com.demo.web.controller.admin;

import com.demo.web.dto.ApiResponse;
import com.demo.web.service.media.MediaStorageClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/media")
@PreAuthorize("hasRole('ADMIN')")
public class MediaUploadController {

    private final MediaStorageClient mediaStorageClient;

    public MediaUploadController(MediaStorageClient mediaStorageClient) {
        this.mediaStorageClient = mediaStorageClient;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Map<String, String>>> upload(@RequestPart("file") MultipartFile file) {
        String url = mediaStorageClient.upload(file);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Upload thành công",
                Map.of("url", url)
        ));
    }
}
