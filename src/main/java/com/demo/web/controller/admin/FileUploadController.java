package com.demo.web.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin/upload")
@PreAuthorize("hasRole('ADMIN')")
public class FileUploadController {

    private final String UPLOAD_DIR = "src/main/resources/static/images/";
    // Fallback to absolute path if running from IDE root
    private final String ABSOLUTE_PATH = "d:/BT_FREELANCE/weddingBooking/src/main/resources/static/images/";

    @PostMapping("/image")
    @ResponseBody
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {
        Map<String, String> response = new HashMap<>();
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // Determine directory based on type (halls, menus, slider)
            String subDir = "others";
            if ("halls".equals(type) || "menus".equals(type) || "slider".equals(type)) {
                subDir = type;
            }

            // Create filename
            String originalName = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = "";
            int dotIndex = originalName.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = originalName.substring(dotIndex);
            }
            String fileName = UUID.randomUUID().toString().substring(0, 8) + extension;

            // Target Paths
            Path relativePath = Paths.get(UPLOAD_DIR, subDir);
            Path absolutePath = Paths.get(ABSOLUTE_PATH, subDir);

            // Ensure directories exist
            if (!Files.exists(absolutePath)) {
                Files.createDirectories(absolutePath);
            }

            // Save file
            Path targetLocation = absolutePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Also try to copy to target/classes if possible/needed (optional, but good for
            // immediate view)
            // For now, just source is enough as devtools might restart or user refreshes.

            String fileUrl = "/images/" + subDir + "/" + fileName;
            response.put("url", fileUrl);
            response.put("message", "Upload successful");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            response.put("error", "Could not upload file: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
