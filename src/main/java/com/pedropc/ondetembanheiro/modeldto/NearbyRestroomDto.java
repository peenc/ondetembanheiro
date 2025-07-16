package com.pedropc.ondetembanheiro.modeldto;

public record NearbyRestroomDto(
        Long id,
        String name,
        String description,
        String type,
        double latitude,
        double longitude,
        double distanciaKm,
        double averageStars
) {}
