package com.pedropc.ondetembanheiro.model;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Embedded
    private Location location;

    private String type;

    // getters e setters
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
