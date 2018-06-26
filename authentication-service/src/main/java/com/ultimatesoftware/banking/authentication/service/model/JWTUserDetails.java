package com.ultimatesoftware.banking.authentication.service.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class JWTUserDetails implements UserDetails {

    private String userName;
    private String token;
    private UUID id;
    private Collection<? extends GrantedAuthority> authorities;


    public JWTUserDetails(String userName, UUID id, String token, List<GrantedAuthority> grantedAuthorities) {

        this.userName = userName;
        this.id = id;
        this.token = token;
        this.authorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public String getToken() {
        return token;
    }


    public UUID getId() {
        return id;
    }

}

