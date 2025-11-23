package com.demo.web.model;

import com.demo.web.model.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "promotions")
public class Promotion extends BaseEntity {

    @NotBlank
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotBlank
    @Column(name = "slug", nullable = false, unique = true, length = 200)
    private String slug;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @FutureOrPresent
    @Column(name = "start_date")
    private LocalDate startDate;

    @FutureOrPresent
    @Column(name = "end_date")
    private LocalDate endDate;

    @Size(max = 1000)
    @Column(name = "terms", length = 1000)
    private String terms;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }
}
