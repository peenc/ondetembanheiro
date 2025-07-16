package com.pedropc.ondetembanheiro.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Restroom extends Place {

    /** Acessível para cadeirantes */
    private Boolean acess;

    /** Banheiro público (ex.: praça, estação) */
    private Boolean publicPlace;

    /** Gratuito (não precisa pagar) */
    private Boolean free;

    /** Possui fraldário para bebê */
    private Boolean babyChangingTable;

    /** Tem papel higiênico disponível */
    private Boolean hasPaper;

    /** Tem sabão para higiene das mãos */
    private Boolean hasSoap;

    /** Tem álcool/antisséptico para as mãos */
    private Boolean hasSanitizer;

    @OneToMany(mappedBy = "restroom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Rating> ratings = new ArrayList<>();

    public double getAverageStars() {
        return ratings.isEmpty() ? 0 : ratings.stream().mapToInt(Rating::getStars).average().orElse(0);
    }

    public List<Rating> getRatings() { return ratings; }
    public void setRatings(List<Rating> ratings) { this.ratings = ratings; }

    public Boolean getAccessible() { return acess; }
    public void setAccessible(Boolean accessible) { this.acess = accessible; }

    public Boolean getPublicPlace() { return publicPlace; }
    public void setPublicPlace(Boolean publicPlace) { this.publicPlace = publicPlace; }

    public Boolean getFree() { return free; }
    public void setFree(Boolean free) { this.free = free; }

    public Boolean getBabyChangingTable() { return babyChangingTable; }
    public void setBabyChangingTable(Boolean babyChangingTable) { this.babyChangingTable = babyChangingTable; }

    public Boolean getHasPaper() { return hasPaper; }
    public void setHasPaper(Boolean hasPaper) { this.hasPaper = hasPaper; }

    public Boolean getHasSoap() { return hasSoap; }
    public void setHasSoap(Boolean hasSoap) { this.hasSoap = hasSoap; }

    public Boolean getHasSanitizer() { return hasSanitizer; }
    public void setHasSanitizer(Boolean hasSanitizer) { this.hasSanitizer = hasSanitizer; }
}

