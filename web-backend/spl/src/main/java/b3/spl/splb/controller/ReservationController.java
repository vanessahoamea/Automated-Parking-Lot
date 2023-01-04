package b3.spl.splb.controller;

import b3.spl.splb.Services.ReservationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reservation")
    public ResponseEntity saveReservation(@RequestBody ObjectNode objectNode) {
        JsonNode parkingSpotIdNode = objectNode.get("parkingSpotId");
        JsonNode userIdNode = objectNode.get("userId");
        if(Objects.isNull(parkingSpotIdNode) || Objects.isNull(userIdNode))
            return ResponseEntity.badRequest().body(null);
        try {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/reservation").toString());
            return ResponseEntity.created(uri).body(reservationService.saveReservation(parkingSpotIdNode.asLong(), userIdNode.asLong()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/reservation/{id}")
    public ResponseEntity removeReservation(@PathVariable("id") Long id) {
        try {
            reservationService.removeReservation(id);
            return ResponseEntity.ok().body("Successful delete");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
