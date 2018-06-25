package com.ultimatesoftware.banking.authorization.service.security;

import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import com.ultimatesoftware.banking.authorization.service.model.JwtAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.FilterChain;
import java.io.IOException;

import static com.ultimatesoftware.banking.authorization.service.security.SecurityConstants.LOGIN_URL;
import static com.ultimatesoftware.banking.authorization.service.security.SecurityConstants.REGISTER_URL;
import static com.ultimatesoftware.banking.authorization.service.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JWTAuthenticationFilter() {
        super(new NegatedRequestMatcher(new OrRequestMatcher(
                new AntPathRequestMatcher(LOGIN_URL),
                new AntPathRequestMatcher(REGISTER_URL))
        ));

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
    }
}