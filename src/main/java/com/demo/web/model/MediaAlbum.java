package com.demo.web.model;

import com.demo.web.model.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "media_albums")
public class MediaAlbum extends BaseEntity {

    @NotBlank
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Size(max = 500)
    @Column(name = "description", length = 500)
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
