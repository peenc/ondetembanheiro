package com.pedropc.ondetembanheiro.controllers;

import com.pedropc.ondetembanheiro.model.Rating;
import com.pedropc.ondetembanheiro.model.Restroom;
import com.pedropc.ondetembanheiro.modeldto.NearbyRestroomDto;
import com.pedropc.ondetembanheiro.modeldto.RestroomDto;
import com.pedropc.ondetembanheiro.services.RatingService;
import com.pedropc.ondetembanheiro.services.RestroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/restrooms")
@CrossOrigin
public class RestroomController {

    private final RestroomService svc;
    private final RatingService svcRat;

    public RestroomController(RestroomService svc, RatingService svcRat) {
        this.svc = svc;
        this.svcRat = svcRat;
    }

    @GetMapping("/proximos")
    public List<NearbyRestroomDto> getNearby(@RequestParam double lat,
                                             @RequestParam double lng,
                                             @RequestParam(defaultValue = "2") double radiusKm) {
        return svc.nearby(lat, lng, radiusKm);
    }



    @GetMapping("/all")
    public List<Restroom> getAllRestrooms() {
        return svc.findAll();
    }

    @PostMapping
    public ResponseEntity<Restroom> create(@Valid @RequestBody RestroomDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(svc.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restroom> details(@PathVariable Long id) {
        Optional<Restroom> banheiro = svc.findById(id);
        return banheiro.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/detalhes")
    public ResponseEntity<?> getDetails(@PathVariable Long id) {
        Restroom r = svc.findById(id).orElse(null);
        if (r == null) return ResponseEntity.notFound().build();

        List<Rating> ratings = svcRat.findByRestroomId(id);
        double avg = ratings.stream().mapToInt(Rating::getStars).average().orElse(0.0);

        Map<String, Object> res = new HashMap<>();
        res.put("restroom", r);
        res.put("ratings", ratings);
        res.put("average", avg);

        return ResponseEntity.ok(res);
    }

}
