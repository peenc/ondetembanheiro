package com.pedropc.ondetembanheiro.controllers;

import com.pedropc.ondetembanheiro.model.Restroom;
import com.pedropc.ondetembanheiro.modeldto.RestroomDto;
import com.pedropc.ondetembanheiro.services.RestroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/restrooms")
@CrossOrigin
public class RestroomController {

    private final RestroomService svc;

    public RestroomController(RestroomService svc) {
        this.svc = svc;
    }

    @GetMapping("/nearby")
    public List<Restroom> getNearby(@RequestParam double lat,
                                    @RequestParam double lng,
                                    @RequestParam(defaultValue = "100000000") double radiusKm) {
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
}
