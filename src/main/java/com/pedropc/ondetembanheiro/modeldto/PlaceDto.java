package com.pedropc.ondetembanheiro.modeldto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlaceDto(
        @NotBlank String name,
        String description,
        @NotNull Double latitude,
        @NotNull Double longitude,
        String type
) {}
