package com.HospitalSystem.Utils;

import com.HospitalSystem.Entity.Patient;
import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.io.Encoder;
//import io.jsonwebtoken.io.Encoders;
//import io.jsonwebtoken.security.Keys;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;

public class JWTUtils {
    public static final long keepTime = 24L * 60L * 60L * 1000L;

    private static final String secretString = "Zd+kZozTI5OgURtbegh8E6KTPghNNe/tEFwuLxd2UNw=";
    //private static final SecretKey KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));


    public static String createToken(Map<String, Object> claims) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        long expMillis = nowMillis + keepTime;

        Date expiredDate = new Date(expMillis);

        String token = Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setSubject("auth")
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secretString)
                .compact();
        return token;
    }

//    //jjwt-api方法
//    public static Patient getPatientFromToken(String token) {
//        Patient patient = new Patient(
//                (String)Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody().get("id"),
//                (String)Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody().get("name"),
//                (String)Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody().get("sex"),
//                (int)Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody().get("age"),
//                new java.sql.Date((long)Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody().get("birthdate")),
//                ""
//        );
//        return patient;
//    }

    //jjwt-0.9.1方法：
    public static Patient getPatientFromToken(String token){
        Patient patient = new Patient(
                (String)Jwts.parser().setSigningKey(secretString).parseClaimsJws(token).getBody().get("id"),
                (String)Jwts.parser().setSigningKey(secretString).parseClaimsJws(token).getBody().get("name"),
                (String)Jwts.parser().setSigningKey(secretString).parseClaimsJws(token).getBody().get("sex"),
                (int)Jwts.parser().setSigningKey(secretString).parseClaimsJws(token).getBody().get("age"),
                new java.sql.Date((long)Jwts.parser().setSigningKey(secretString).parseClaimsJws(token).getBody().get("birthdate")),
                ""
        );
        return patient;
    }

}
