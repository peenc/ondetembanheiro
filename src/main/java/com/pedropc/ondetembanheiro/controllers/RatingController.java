package com.pedropc.ondetembanheiro.controllers;

import com.pedropc.ondetembanheiro.model.Rating;
import com.pedropc.ondetembanheiro.model.Restroom;
import com.pedropc.ondetembanheiro.modeldto.RatingDto;
import com.pedropc.ondetembanheiro.modeldto.RatingSummaryDto;
import com.pedropc.ondetembanheiro.repositories.RatingRepository;
import com.pedropc.ondetembanheiro.repositories.RestroomRepository;
import com.pedropc.ondetembanheiro.services.RatingService;
import com.pedropc.ondetembanheiro.services.RestroomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/restrooms")
public class RatingController {

    private final RatingService ratingService;

    public RatingController( RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /** POST /api/restrooms/{id}/ratings */
    @PostMapping("/{id}/ratings")
    public ResponseEntity<?> addRating(@PathVariable Long id,
                                       @RequestBody RatingDto dto) {
        ratingService.addRating(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /** GET /api/restrooms/{id}/ratings */
    @GetMapping("/{id}/ratings")
    public RatingSummaryDto ratings(@PathVariable Long id) {
        return ratingService.getSummary(id);
    }
}
