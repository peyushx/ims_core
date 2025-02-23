package rapifuzz.com.ims.security;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import rapifuzz.com.ims.constants.ErrorConstants;
import rapifuzz.com.ims.exception.CustomException;
import rapifuzz.com.ims.model.entity.UserEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${secret.key}")
    private String SECRET_KEY;


    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public String createToken(String email) {
        log.info("Creating new token for user :: "+email);
        Instant instant = Instant.now();
        Date expiredDate = Date.from(instant.plus(5l, ChronoUnit.DAYS));
        String token = Jwts.builder()
                .claim("email", email)
                .setIssuedAt(Date.from(instant))
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        return token;
    }



    /**
     * Method to validate token
     *
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            token = token.replace("Bearer ", "");
            Jwts.parser().setSigningKey(SECRET_KEY).parse(token);
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("invalid token passed");
            if (e.getMessage() != null && e.getMessage().startsWith("JWT expired at")) {
                throw new CustomException(ErrorConstants.INVALID_OR_EXPIRED_JWT_TOKEN, HttpStatus.UNAUTHORIZED);
            } else {
                log.info("invalid token passed");
                throw new CustomException(ErrorConstants.INVALID_OR_EXPIRED_JWT_TOKEN, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    public String getUserEmail(String token) {
        token = token.replace("Bearer ", "");
        String email = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("email", String.class);
        return email;
    }

}
