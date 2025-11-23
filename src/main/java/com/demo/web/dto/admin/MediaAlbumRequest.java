package com.demo.web.dto.admin;

import jakarta.validation.constraints.NotBlank;

public class MediaAlbumRequest {

    @NotBlank
    private String title;

    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
