package com.pedropc.ondetembanheiro.repositories;

import com.pedropc.ondetembanheiro.model.Restroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestroomRepository extends JpaRepository<Restroom, Long> {

      @Query(value = """
    SELECT r.*,
           ( 6371 * acos(
               cos(radians(:lat)) * cos(radians(r.latitude)) *
               cos(radians(r.longitude) - radians(:lng)) +
               sin(radians(:lat)) * sin(radians(r.latitude))
             ) ) AS distance
    FROM restroom r
    WHERE ( 6371 * acos(
               cos(radians(:lat)) * cos(radians(r.latitude)) *
               cos(radians(r.longitude) - radians(:lng)) +
               sin(radians(:lat)) * sin(radians(r.latitude))
           ) ) < :radius
    ORDER BY distance
  """, nativeQuery = true)
    List<Restroom> findNearby(@Param("lat") double lat,
                              @Param("lng") double lng,
                              @Param("radius") double radiusKm);
}