package com.ultimatesoftware.banking.authentication.service.security;

import com.ultimatesoftware.banking.authentication.service.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import static com.ultimatesoftware.banking.authentication.service.model.SecurityConstants.SECRET;


@Component
public class JWTGenerator {


    public String generate(User user) {


        Claims claims = Jwts.claims()

                .setSubject(user.getUserName());
        claims.put("userId", String.valueOf(user.getId()));
        claims.put("role", user.getRole());


        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
}

