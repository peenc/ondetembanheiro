package com.pedropc.ondetembanheiro.services;

import com.pedropc.ondetembanheiro.model.Location;
import com.pedropc.ondetembanheiro.model.Restroom;
import com.pedropc.ondetembanheiro.modeldto.RestroomDto;
import com.pedropc.ondetembanheiro.repositories.RestroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestroomService {
    private final RestroomRepository repo;

    public RestroomService(RestroomRepository repo) {
        this.repo = repo;
    }

    public List<Restroom> nearby(double lat, double lng, double radiusKm) {
        return repo.findNearby(lat, lng, radiusKm);
    }

    public Restroom save(RestroomDto dto) {
        Restroom r = new Restroom();
        r.setName(dto.name());
        r.setDescription(dto.description());
        r.setAccessible(dto.acess());
        r.setPublicPlace(dto.publicPlace());
        r.setFree(dto.free());
        r.setBabyChangingTable(dto.babyChangingTable());
        r.setHasPaper(dto.hasPaper());
        r.setHasSoap(dto.hasSoap());
        r.setHasSanitizer(dto.hasSanitizer());
        r.setType(dto.type());

        Location loc = new Location();
        loc.setLatitude(dto.latitude());
        loc.setLongitude(dto.longitude());
        r.setLocation(loc);

        return repo.save(r);
    }


    public List<Restroom> findAll() {
        return repo.findAll();
    }
}