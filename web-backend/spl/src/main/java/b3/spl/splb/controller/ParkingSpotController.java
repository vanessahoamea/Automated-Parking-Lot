package b3.spl.splb.controller;

import b3.spl.splb.Services.ParkingSpotService;
import b3.spl.splb.model.ParkingSpot;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ParkingSpotController {
    private final ParkingSpotService parkingSpotService;

    @GetMapping("/parkingspots")
    public ResponseEntity<List<ParkingSpot>> getParkingSpots() {
        return ResponseEntity.ok().body(parkingSpotService.getParkingSpots());
    }

    @GetMapping("parkingspots/{id}")
    public ResponseEntity getParkingSpot(@PathVariable("id") Long id) {
        ParkingSpot result = parkingSpotService.getParkingSpot(id);
        if(Objects.isNull(result)) {
            return ResponseEntity.badRequest().body("No parking spot with id " + id + " exists!");
        }
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/parkingspots")
    public ResponseEntity<ParkingSpot> saveParkingSpot(@RequestBody ParkingSpot parkingSpot) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/save/parkinglot").toString());
        return ResponseEntity.created(uri).body(parkingSpotService.saveParkingSpot(parkingSpot));
    }
    
    @PutMapping("/parkingspots/{id}")
    public ResponseEntity updateParkingSpot(@RequestBody ParkingSpot parkingSpot, @PathVariable("id") Long id) {
        if(Objects.isNull(parkingSpotService.getParkingSpot(id))) {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/parkinglots").toString());
            return ResponseEntity.created(uri).body(parkingSpotService.saveParkingSpot(parkingSpot));
        }
        return ResponseEntity.ok().body(parkingSpotService.updateParkingSpot(parkingSpot, id));
    }

    @DeleteMapping("/parkingspots/{id}")
    public ResponseEntity<String> deleteParkingSpot(@PathVariable Long id) {
        try {
            parkingSpotService.deleteParkingSpot(id);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body("No parking spot with id " + id + " exists!");
        }
        return ResponseEntity.ok().body("Successful delete");
    }
}
