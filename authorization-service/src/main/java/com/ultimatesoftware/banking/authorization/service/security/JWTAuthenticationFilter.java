package com.ultimatesoftware.banking.authorization.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ultimatesoftware.banking.authorization.service.model.JwtAuthenticationToken;
import com.ultimatesoftware.banking.authorization.service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.ultimatesoftware.banking.authorization.service.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {
    @Autowired
    private AuthenticationManager authenticationManager;
    public JwtAuthenticationTokenFilter() {
        super("/**");

    }
    public JwtAuthenticationTokenFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        String header = httpServletRequest.getHeader("Authorization");

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            throw new RuntimeException("JWT Missing or Incorrect Header");
        }

        String authenticationToken = header.substring(7);

        JwtAuthenticationToken token = new JwtAuthenticationToken(authenticationToken);
        return getAuthenticationManager().authenticate(token);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
        //TokenAuthenticationService.addAuthentication(response,(((User)authResult.getPrincipal()).getUserName()));
    }
}