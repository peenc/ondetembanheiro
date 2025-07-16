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
    SELECT 
        r.id,
        r.name,
        r.description,
        r.type,
        r.latitude,
        r.longitude,
        (6371 * acos(
            cos(radians(:lat)) * cos(radians(r.latitude)) *
            cos(radians(r.longitude) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(r.latitude))
        )) AS distanciaKm,
        COALESCE(AVG(rt.stars), 0) AS averageStars
    FROM restroom r
    LEFT JOIN rating rt ON rt.restroom_id = r.id
    WHERE (6371 * acos(
            cos(radians(:lat)) * cos(radians(r.latitude)) *
            cos(radians(r.longitude) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(r.latitude))
        )) < :radius
    GROUP BY r.id, r.name, r.description, r.type, r.latitude, r.longitude
    ORDER BY distanciaKm
""", nativeQuery = true)
    List<Object[]> findNearbyRaw(@Param("lat") double lat,
                                 @Param("lng") double lng,
                                 @Param("radius") double radiusKm);

}