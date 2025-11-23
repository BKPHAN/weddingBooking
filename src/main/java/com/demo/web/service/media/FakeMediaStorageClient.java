package com.demo.web.service.media;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@Component
public class FakeMediaStorageClient implements MediaStorageClient {

    private static final Logger log = LoggerFactory.getLogger(FakeMediaStorageClient.class);

    @Override
    public String upload(MultipartFile file) {
        String generatedUrl = "https://fake-storage.local/" + Instant.now().toEpochMilli() + "-" + UUID.randomUUID();
        log.info("Fake upload media file: name={}, size={} -> {}", file.getOriginalFilename(), file.getSize(), generatedUrl);
        return generatedUrl;
    }
}
