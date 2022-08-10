package com.marsh.sqlmateapi.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public class JwtHelper {

    private static final String secret = "1111";

    public static String encode(String subject, Integer expireDays, String audience, Map<String, String> claims) {
        Map<String, Object> header = Map.of(
                "alg", "HS256",
                "typ", "JWT"
        );
        var time = System.currentTimeMillis();
        try {
            var algorithm = Algorithm.HMAC256(secret);
            var jb = JWT.create()
                    .withIssuer("auth0")
                    .withHeader(header)
                    .withIssuedAt(new Date(time))
                    .withSubject(subject)
                    .withAudience(audience)
                    .withExpiresAt(new Date(time + expireDays * 1000 * 60 * 60 * 24))
                    .withAudience();
            claims.forEach(jb::withClaim);
            return jb.sign(algorithm);
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return null;
    }

    public static DecodedJWT decode(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); //use more secure key
            var verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
        }
        return null;
    }

}
