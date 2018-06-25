package com.ultimatesoftware.banking.authorization.service.security;

import com.ultimatesoftware.banking.authorization.service.model.User;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.UUID;

import static com.ultimatesoftware.banking.authorization.service.security.SecurityConstants.SECRET;

@Component
public class JwtValidator {


    public User validate(String token) {

        User user = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();

            user = new User();

            user.setUserName(body.getSubject());
            user.setId(UUID.fromString((String)body.get("userId")));
            user.setRole((String) body.get("role"));
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return user;
    }
}