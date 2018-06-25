package com.ultimatesoftware.banking.authorization.service.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken{

    private String token;


    public JwtAuthenticationToken(String token) {
        super(null, null);
        this.token = token;
    }
    public JwtAuthenticationToken(String token, User user) {
        super(user, null, null);
        this.token = token;
    }

//public JwtAuthenticationToken(String token, Object principal, Object credentials, Collection<? extends GrantedAuthority> authority){super(principal,credentials,authority);}

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