package com.ultimatesoftware.banking.authorization.service.config;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import com.ultimatesoftware.banking.authorization.service.security.JwtAuthenticationEntryPoint;
import com.ultimatesoftware.banking.authorization.service.security.JWTAuthenticationProvider;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.ultimatesoftware.banking.authorization.service.security.JWTAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import com.ultimatesoftware.banking.authorization.service.security.JwtSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.util.Collections;

import static com.ultimatesoftware.banking.authorization.service.security.SecurityConstants.LOGIN_URL;
import static com.ultimatesoftware.banking.authorization.service.security.SecurityConstants.REGISTER_URL;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JWTSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTAuthenticationProvider authenticationProvider;
    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;


    @Bean
    public AuthenticationManager authenticationManager() {
       return new ProviderManager(Collections.singletonList(authenticationProvider));

    }

    @Bean
    public JWTAuthenticationFilter authenticationTokenFilter() {
        JWTAuthenticationFilter filter = new JWTAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
        return filter;
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.POST, LOGIN_URL, REGISTER_URL);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().and().cors().disable()
                .authorizeRequests()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
                http.headers().cacheControl();
    }


}