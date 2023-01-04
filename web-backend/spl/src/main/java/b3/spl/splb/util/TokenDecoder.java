package b3.spl.splb.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;



@NoArgsConstructor
public class TokenDecoder {

    public String getEmailFromToken(HttpHeaders headers){
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION).substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());//TODO : de mutat in fisier de configurare
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

}
