package com.ultimatesoftware.banking.authentication.service.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class JWTAuthenticationToken extends UsernamePasswordAuthenticationToken{

    private String token;


    public JWTAuthenticationToken(String token) {
        super(null, null);
        this.token = token;
    }
    public JWTAuthenticationToken(String token, User user) {
        super(user, null, null);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

}
