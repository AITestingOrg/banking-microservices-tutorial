package com.ultimatesoftware.banking.authorization.service.security;

import com.ultimatesoftware.banking.authorization.service.model.JWTAuthenticationToken;
import com.ultimatesoftware.banking.authorization.service.model.JWTUserDetails;
import com.ultimatesoftware.banking.authorization.service.model.User;
import com.ultimatesoftware.banking.authorization.service.services.ServiceUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JWTAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private JWTValidator validator;

    @Autowired
    private ServiceUserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        JWTAuthenticationToken jwtAuthenticationToken = (JWTAuthenticationToken) usernamePasswordAuthenticationToken;
        String token = jwtAuthenticationToken.getToken();

        User user = validator.validate(token);

        if(user == null) {
            throw new RuntimeException("JWT Token is incorrect");
        }

        else if (userRepository.exists(user.getId())) {
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                    .commaSeparatedStringToAuthorityList(user.getRole());
            return new JWTUserDetails(user.getUserName(), user.getId(),
                    token,
                    grantedAuthorities);
        }
        else throw new RuntimeException("User was not found");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JWTAuthenticationToken.class.isAssignableFrom(aClass));
    }
}