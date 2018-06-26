package com.ultimatesoftware.banking.authentication.service.security;

import com.ultimatesoftware.banking.authentication.service.model.JWTAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.ultimatesoftware.banking.authentication.service.model.SecurityConstants.*;


public class JWTAuthorization extends AbstractAuthenticationProcessingFilter {

    public JWTAuthorization() {
        super(new NegatedRequestMatcher(new OrRequestMatcher(
                new AntPathRequestMatcher(LOGIN_URL),
                new AntPathRequestMatcher(REGISTER_URL))
        ));

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        String header = httpServletRequest.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            throw new RuntimeException("JWT Missing or Incorrect Header");
        }

        String authenticationToken = header.substring(7);

        JWTAuthenticationToken token = new JWTAuthenticationToken(authenticationToken);
        return getAuthenticationManager().authenticate(token);

    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
