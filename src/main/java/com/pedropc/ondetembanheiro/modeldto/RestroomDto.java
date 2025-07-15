package com.pedropc.ondetembanheiro.modeldto;

import jakarta.validation.constraints.*;

public record RestroomDto(
        @NotBlank String name,
        String description,
        @NotNull Double latitude,
        @NotNull Double longitude,
        boolean acess,
        boolean publicPlace,
        boolean free,
        boolean babyChangingTable,
        boolean hasPaper,
        boolean hasSoap,
        boolean hasSanitizer,
        String type){

}