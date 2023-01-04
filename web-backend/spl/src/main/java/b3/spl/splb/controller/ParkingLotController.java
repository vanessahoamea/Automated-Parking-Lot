package b3.spl.splb.controller;

import b3.spl.splb.Services.AppUserService;
import b3.spl.splb.Services.ParkingLotService;
import b3.spl.splb.model.ParkingLot;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import b3.spl.splb.util.Point;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public  class ParkingLotController {
    private final ParkingLotService parkingLotService;
    private final AppUserService appUserService;
    private String getEmailFromToken(HttpHeaders headers){
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());//TODO : de mutat in fisier de configurare
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

    @PostMapping("provider/parkinglot/save")
    public ResponseEntity<?> saveParkinglot(@RequestHeader HttpHeaders headers, @RequestBody ParkingLot parkingLot){
        String email = getEmailFromToken(headers);
        if(parkingLot == null || parkingLot.getName() == null || parkingLot.getLatitude() == null || parkingLot.getLongitude() == null){
            return ResponseEntity.badRequest().body("Invalid input.");
        }

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/provider/parkinglot/save").toString());
        ParkingLot savedParkingLot = parkingLotService.saveParkingLot(parkingLot, email);
        if(savedParkingLot == null) {
            return ResponseEntity.badRequest().body("Could not save parking lot.");
        }
        return ResponseEntity.created(uri).body(savedParkingLot);
    }

    @GetMapping("provider/parkinglots")
    public ResponseEntity<List<ParkingLot>> getParkingLots(@RequestHeader HttpHeaders headers){
        String email = getEmailFromToken(headers);
        return ResponseEntity.ok().body(appUserService.getUser(email).getParkingLots());
    }

    @PostMapping("/user/parkinglots")
    public ResponseEntity<?> getClosestParkingLots(@RequestBody Point userLocation){
        if(userLocation == null || userLocation.getLatitude() == null || userLocation.getLongitude() == null){
            return ResponseEntity.badRequest().body("Invalid input.");
        }
        return ResponseEntity.ok().body(parkingLotService.getClosestParkingLots(userLocation));
    }

    @PostMapping("/parkinglot/add/parkingspot")
    public ResponseEntity<?> addParkingSpotToParkingLot(@RequestBody ObjectNode objectNode){
        if(objectNode.has("lot_id") && objectNode.has("spot_id")) {
            Long lot_id = objectNode.get("lot_id").asLong();
            Long spot_id = objectNode.get("spot_id").asLong();
            try {
                if(parkingLotService.addParkingSpot(lot_id, spot_id) == true){
                    return ResponseEntity.ok().body("Success!");
                }
                else {
                    return ResponseEntity.badRequest().body("Lot or spot not found");
                }
            }catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }


        }
        return ResponseEntity.badRequest().body("Spot id and lot id must be provided.");
    }

    @DeleteMapping("/parkinglot/{id}")
    public ResponseEntity<String> deleteParkingSpot(@PathVariable Long id) {
        try {
            parkingLotService.deleteParkingLot(id);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body("No parking lot with id " + id + " exists!");
        }
        return ResponseEntity.ok().body("Successful delete");
    }


    @GetMapping("admin/parkinglot/approve")
    public ResponseEntity<?> setApproved(@RequestBody ObjectNode objectNode){
        if(objectNode.has("id") && objectNode.has("approved")) {
            Long id = objectNode.get("id").asLong();
            Boolean approved = objectNode.get("approved").asBoolean();
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/admin/parkinglot/approve").toString());
            Optional<ParkingLot> parkingLot = parkingLotService.setApproved(id, approved);
            if(!parkingLot.isPresent()) {
                return ResponseEntity.badRequest().body("Parking lot not found.");
            }
            return ResponseEntity.created(uri).body(parkingLot);
        }
        return ResponseEntity.badRequest().body("id and approved value must be provided.");
    }
}
