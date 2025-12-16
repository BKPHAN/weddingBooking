package com.demo.web.model;

import com.demo.web.model.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "menus")
public class Menu extends BaseEntity {

    @NotBlank
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @NotNull
    @Positive
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_featured")
    private Boolean featured = false;

    @Column(name = "type", length = 50)
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
