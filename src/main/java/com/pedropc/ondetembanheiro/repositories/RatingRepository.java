package com.pedropc.ondetembanheiro.repositories;

import com.pedropc.ondetembanheiro.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByRestroomId(Long restroomId);
}
