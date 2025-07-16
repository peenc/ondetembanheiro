package com.pedropc.ondetembanheiro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


import java.time.LocalDateTime;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1) @Max(5)
    private int stars;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference          // impede voltar para Restroom na serialização
    private Restroom restroom;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Restroom getRestroom() {
        return restroom;
    }

    public void setRestroom(Restroom restroom) {
        this.restroom = restroom;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Min(1)
    @Max(5)
    public int getStars() {
        return stars;
    }

    public void setStars(@Min(1) @Max(5) int stars) {
        this.stars = stars;
    }
}
