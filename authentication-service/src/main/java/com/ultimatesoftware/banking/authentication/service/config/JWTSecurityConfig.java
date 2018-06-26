package com.ultimatesoftware.banking.authentication.service.config;

import com.ultimatesoftware.banking.authentication.service.security.JWTAuthenticationEntryPoint;
import com.ultimatesoftware.banking.authentication.service.security.JWTAuthenticationProvider;
import com.ultimatesoftware.banking.authentication.service.security.JWTAuthorization;
import com.ultimatesoftware.banking.authentication.service.security.JWTSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

import static com.ultimatesoftware.banking.authentication.service.model.SecurityConstants.LOGIN_URL;
import static com.ultimatesoftware.banking.authentication.service.model.SecurityConstants.REGISTER_URL;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JWTSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTAuthenticationProvider authenticationProvider;
    @Autowired
    private JWTAuthenticationEntryPoint entryPoint;


    @Bean
    public AuthenticationManager authenticationManager() {
       return new ProviderManager(Collections.singletonList(authenticationProvider));

    }

    @Bean
    public JWTAuthorization authenticationTokenFilter() {
        JWTAuthorization filter = new JWTAuthorization();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new JWTSuccessHandler());
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
