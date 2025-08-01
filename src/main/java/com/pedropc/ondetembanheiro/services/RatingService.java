package com.pedropc.ondetembanheiro.services;

import com.pedropc.ondetembanheiro.model.Rating;
import com.pedropc.ondetembanheiro.model.Restroom;
import com.pedropc.ondetembanheiro.modeldto.RatingDto;
import com.pedropc.ondetembanheiro.modeldto.RatingSummaryDto;
import com.pedropc.ondetembanheiro.repositories.RatingRepository;
import com.pedropc.ondetembanheiro.repositories.RestroomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RatingService {

    private final RestroomRepository restroomRepo;
    private final RatingRepository ratingRepo;

    public RatingService(RestroomRepository restroomRepo, RatingRepository ratingRepo) {
        this.restroomRepo = restroomRepo;
        this.ratingRepo = ratingRepo;
    }

    public void addRating(Long restroomId, RatingDto dto) {
        Restroom restroom = restroomRepo.findById(restroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Rating rating = new Rating();
        rating.setStars(dto.stars());
        rating.setComment(dto.comment());
        rating.setRestroom(restroom);

        ratingRepo.save(rating);
    }

    public RatingSummaryDto getSummary(Long restroomId) {
        Restroom r = restroomRepo.findById(restroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new RatingSummaryDto(r.getAverageStars(), r.getRatings());
    }

    public List<Rating> findByRestroomId(Long restroomId) {
        return ratingRepo.findByRestroomId(restroomId);
    }
}
