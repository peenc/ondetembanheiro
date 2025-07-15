package com.pedropc.ondetembanheiro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Restroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    /** Localização (latitude/longitude) */
    @Embedded
    private Location location;

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

    /** Tipo: Shopping, Restaurante, Parque, Rodoviária, Outros… */
    private String type;

    /*‑‑‑ Getters & Setters ‑‑‑*/

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

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

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

