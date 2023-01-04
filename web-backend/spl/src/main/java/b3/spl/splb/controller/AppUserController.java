package b3.spl.splb.controller;
import b3.spl.splb.Services.AppUserService;
import b3.spl.splb.model.AppUser;
import b3.spl.splb.model.Car;
import b3.spl.splb.model.ParkingLot;
import b3.spl.splb.model.Role;
import b3.spl.splb.util.TokenDecoder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Console;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppUserController {
    private final AppUserService appUserService;
    private final TokenDecoder tokenDecoder = new TokenDecoder();

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers(){
        return ResponseEntity.ok().body(appUserService.getUsers());
    }

    @GetMapping("/user/profile")
    public ResponseEntity<AppUser> getUserProfile(@RequestHeader HttpHeaders headers){
        return ResponseEntity.ok().body(appUserService.getUser(tokenDecoder.getEmailFromToken(headers)));
    }
    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(@RequestHeader HttpHeaders headers){
        try {
            AppUser user = appUserService.getUser(tokenDecoder.getEmailFromToken(headers));
            appUserService.deleteUser(user);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Could not delete user!");
        }
        return  ResponseEntity.ok().body("User deleted successfully!");
    }

    @PutMapping("user/restepassword")
    public ResponseEntity<?> updatePassword(@RequestHeader HttpHeaders headers, @RequestBody ObjectNode objectNode){

        try {
            String oldPassword = objectNode.get("old_password").textValue();
            String newPassword = objectNode.get("new_password").textValue();

            AppUser user = appUserService.getUser(tokenDecoder.getEmailFromToken(headers));
            if(!appUserService.checkIfValidOldPassword(user, oldPassword)){
                throw new  Exception("The old password is incorrect!");
            }
            if(!appUserService.changeUserPassword(user, newPassword)){
                throw new Exception("New password did not meet the complexity requirements!");
            }
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Password was changed successfully!");
    }

    @PostMapping("/user/save")
    public ResponseEntity saveUser(@RequestBody AppUser user){
        System.out.println(user);
        if(user == null || user.getUsername() == null || user.getPassword() == null || user.getName() == null || user.getEmail() == null){
            return ResponseEntity.badRequest().body("Invalid input.");
        }
        user.setEmail(user.getEmail().trim().toLowerCase());

        if(!user.getEmail().matches("[a-zA-Z0-9_\\.-]+@[a-zA-Z0-9]+(\\.[a-zA-Z0-9_-]{2,4})+")){
            return ResponseEntity.badRequest().body("Invalid email.");
        }

        if(user.getPassword().length()<8 || !user.getPassword().matches(".*[A-Z].*") ||
                !user.getPassword().matches(".*[a-z].*") || !user.getPassword().matches(".*[0-9].*")){
            return ResponseEntity.badRequest().body("Invalid password.");
        }

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toString());
        AppUser resp = appUserService.saveUser(user);
        if(resp == null){
            return ResponseEntity.badRequest().body("This email address is already being used.");
        }
        return ResponseEntity.created(uri).body(resp);
    }

    @PutMapping("/user")
    public ResponseEntity updateUser(@RequestHeader HttpHeaders httpHeaders, @RequestBody AppUser user) {
        AppUser updatedUser = appUserService.getUser(tokenDecoder.getEmailFromToken(httpHeaders));
        try {
            appUserService.updateUser(updatedUser, user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("User updated!");
    }


    @PostMapping("/add/role")
    public ResponseEntity addRole(@RequestBody ObjectNode objectNode){
        if(objectNode.has("email") && objectNode.has("role")){
            String email = objectNode.get("email").textValue();
            String role = objectNode.get("role").textValue();
            System.out.println(role);
            if(appUserService.addRoleToAppUser(email, role) == false)
                return ResponseEntity.badRequest().body("User or role not found.");
            String s = "Role added.";
            return ResponseEntity.ok().body("Role added.");}
        return ResponseEntity.badRequest().body("Email and role must be provided.");
    }

    @PostMapping("/admin/banstatus")
    public ResponseEntity banUser(@RequestBody ObjectNode objectNode){
        if(objectNode.has("email") && objectNode.has("banUser")) {
            String email = objectNode.get("email").asText();
            Boolean banned = objectNode.get("banUser").asBoolean();

            if(appUserService.setBannedUser(email, banned)){
                ResponseEntity.ok().body("Banned user.");
            }else return ResponseEntity.badRequest().body("User not found.");

        }
        return ResponseEntity.badRequest().body("email and banUser must be provided.");

    }
    @PostMapping("/admin/banprovider")
    public ResponseEntity banProvider(@RequestBody ObjectNode objectNode){
        if(objectNode.has("email") && objectNode.has("banned")) {
            String email = objectNode.get("email").asText();
            Boolean banned = objectNode.get("banProvider").asBoolean();

            if(appUserService.setBannedProvider(email, banned)){
                ResponseEntity.ok().body("Banned provider.");
            }else return ResponseEntity.badRequest().body("Provider not found.");

        }
        return ResponseEntity.badRequest().body("email zand banProvider must be provided.");

    }

    @PostMapping("/user/add/car")
    public ResponseEntity addCar(@RequestBody ObjectNode objectNode){
        if(objectNode.has("carId") && objectNode.has("userId")){
            Long carId = objectNode.get("carId").asLong();
            Long userId = objectNode.get("userId").asLong();
            if(appUserService.addCarToUser(carId, userId) == false)
                return ResponseEntity.badRequest().body("User or car not found.");
            return ResponseEntity.ok().body("Car added.");
        }
        return ResponseEntity.badRequest().body("carId and userId must be provided.");
    }


    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());//TODO : de mutat in fisier de configurare
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                AppUser user = appUserService.getUser(username);

                String access_token = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            }
            catch (Exception exception){
                response.setHeader("error", exception.getMessage());
                response.setStatus(403);
                Map<String, String> error = new HashMap<>();
                error.put("error", exception.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
    }

    @GetMapping("/user/cars")
    public ResponseEntity<?> getUserCars(@RequestHeader HttpHeaders headers) {
        String email = tokenDecoder.getEmailFromToken(headers);
        List<Car> cars = appUserService.getUserCars(email);
        return ResponseEntity.ok().body(cars);
    }
}

@Data
class RoleToUserForm{
    private String email;
    private String roleName;
}
