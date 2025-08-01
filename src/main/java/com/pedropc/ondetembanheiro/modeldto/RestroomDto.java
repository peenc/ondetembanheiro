package com.pedropc.ondetembanheiro.modeldto;

import jakarta.validation.constraints.*;
public record RestroomDto(
        PlaceDto place,
        boolean acess,
        boolean publicPlace,
        boolean free,
        boolean babyChangingTable,
        boolean hasPaper,
        boolean hasSoap,
        boolean hasSanitizer,
        double averageStars
) {}
