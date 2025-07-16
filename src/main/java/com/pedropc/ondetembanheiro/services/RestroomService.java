package com.pedropc.ondetembanheiro.services;

import com.pedropc.ondetembanheiro.model.Location;
import com.pedropc.ondetembanheiro.model.Restroom;
import com.pedropc.ondetembanheiro.modeldto.NearbyRestroomDto;
import com.pedropc.ondetembanheiro.modeldto.RestroomDto;
import com.pedropc.ondetembanheiro.repositories.RestroomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestroomService {
    private final RestroomRepository repo;

    public RestroomService(RestroomRepository repo) {
        this.repo = repo;
    }

    public List<NearbyRestroomDto> nearby(double lat, double lng, double radiusKm) {
        List<Object[]> rows = repo.findNearbyRaw(lat, lng, radiusKm);

        return rows.stream()
                .map(r -> new NearbyRestroomDto(
                        ((Number) r[0]).longValue(),     // id
                        (String) r[1],                   // name
                        (String) r[2],                   // description
                        (String) r[3],                   // type
                        ((Number) r[4]).doubleValue(),   // latitude
                        ((Number) r[5]).doubleValue(),   // longitude
                        ((Number) r[6]).doubleValue(),   // distanciaKm
                        ((Number) r[7]).doubleValue()    // averageStars
                ))
                .toList();
    }


    public Restroom save(RestroomDto dto) {
        Restroom r = new Restroom();
        r.setName(dto.place().name());
        r.setDescription(dto.place().description());
        r.setType(dto.place().type());

        // localização herdada de Place
        r.setLocation(new Location(dto.place().latitude(), dto.place().longitude()));

        // atributos específicos de Restroom
        r.setAccessible(dto.acess());
        r.setPublicPlace(dto.publicPlace());
        r.setFree(dto.free());
        r.setBabyChangingTable(dto.babyChangingTable());
        r.setHasPaper(dto.hasPaper());
        r.setHasSoap(dto.hasSoap());
        r.setHasSanitizer(dto.hasSanitizer());

        return repo.save(r);
    }



    public Optional<Restroom> findById(long id) {
        return repo.findById(id);
    }

    public List<Restroom> findAll() {
        return repo.findAll();
    }
}