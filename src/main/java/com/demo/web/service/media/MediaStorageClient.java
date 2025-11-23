package com.demo.web.service.media;

import org.springframework.web.multipart.MultipartFile;

public interface MediaStorageClient {

    /**
     * Upload file to storage and return accessible URL.
     *
     * @param file multipart file
     * @return URL (string)
     */
    String upload(MultipartFile file);
}
