package com.ultimatesoftware.banking.authorization.service.security;

import com.ultimatesoftware.banking.authorization.service.model.User;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import static com.ultimatesoftware.banking.authorization.service.security.SecurityConstants.SECRET;


@Component
public class JwtGenerator {


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

