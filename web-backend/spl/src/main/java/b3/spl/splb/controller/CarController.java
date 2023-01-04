package b3.spl.splb.controller;

import b3.spl.splb.Services.CarService;
import b3.spl.splb.model.Car;
import b3.spl.splb.util.TokenDecoder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/car")
public class CarController {
    private final CarService carService;
    private final TokenDecoder tokenDecoder = new TokenDecoder();

    @PostMapping()
    public ResponseEntity<?> saveCar(@RequestHeader HttpHeaders headers, @RequestBody ObjectNode objectNode){
        String email = tokenDecoder.getEmailFromToken(headers);
        JsonNode licensePlateNode = objectNode.get("licensePlate");
        String licensePlate;
        if(objectNode == null)
            return ResponseEntity.badRequest().body("Invalid input");
        if (licensePlateNode == null)
            return ResponseEntity.badRequest().body("Invalid input format");
        else licensePlate = licensePlateNode.asText();

        Pattern pattern = Pattern.compile("[A-Z]+[0-9]+[A-Z]+");
        boolean isValid= Pattern.matches(String.valueOf(pattern), licensePlate);

        if(!isValid)
            return ResponseEntity.badRequest().body("Invalid input");
        else {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/car/save").toString());
            Car car = new Car();
            car.setLicensePlate(licensePlate);

            return ResponseEntity.created(uri).body(carService.saveCar(email, car));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCarById(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        if (car == null)
            return new ResponseEntity<>("Car with id " + id + " couldn't be found.",HttpStatus.NOT_FOUND);
        return ResponseEntity.ok().body(car);
    }
    @PutMapping()
    public ResponseEntity<?> updateCar(@RequestBody ObjectNode objectNode)
    {
        if(!objectNode.has("carId") || ! objectNode.has("newLicensePlate"))
            return ResponseEntity.badRequest().body("Invalid input format");
        JsonNode carIdNode = objectNode.get("carId");
        JsonNode newLicensePlateNode = objectNode.get("newLicensePlate");
        String newLicensePlate;
        Long carId;

        if (carIdNode == null)
            return ResponseEntity.badRequest().body("Invalid input format");
        else carId = carIdNode.asLong();

        if (newLicensePlateNode == null)
            return ResponseEntity.badRequest().body("Invalid input format");
        else newLicensePlate = newLicensePlateNode.asText();

        Pattern pattern = Pattern.compile("[A-Z]+[0-9]+[A-Z]+");
        boolean isValid= Pattern.matches(String.valueOf(pattern), newLicensePlate);

        if(!isValid)
            return ResponseEntity.badRequest().body("Invalid license plate format: " + newLicensePlate);
        else {
            if(carService.getCarById(carId) == null)
                return new ResponseEntity<>("Car with id " + carId + " couldn't be found.",HttpStatus.NOT_FOUND);
            else
                return ResponseEntity.ok().body(carService.updateCarLicensePlate(carId, newLicensePlate));
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        if (!carService.deleteCar(id))
            return new ResponseEntity<>("Car with id " + id + " couldn't be found.",HttpStatus.NOT_FOUND);
        return new ResponseEntity<>("Car with id " + id + " deleted successfully.",HttpStatus.OK);
    }

}
