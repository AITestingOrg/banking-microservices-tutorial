package com.ultimatesoftware.banking.authorization.service.security;

import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import com.ultimatesoftware.banking.authorization.service.model.JwtAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.ultimatesoftware.banking.authorization.service.User.ServiceUserRepository;
import com.ultimatesoftware.banking.authorization.service.model.JwtUserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.ultimatesoftware.banking.authorization.service.model.User;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JWTAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private JwtValidator validator;

    @Autowired
    private ServiceUserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken;
        String token = jwtAuthenticationToken.getToken();

        User user = validator.validate(token);

        if(user == null) {
            throw new RuntimeException("JWT Token is incorrect");
        }

        else if (userRepository.exists(user.getId())) {
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                    .commaSeparatedStringToAuthorityList(user.getRole());
            return new JwtUserDetails(user.getUserName(), user.getId(),
                    token,
                    grantedAuthorities);
        }
        else throw new RuntimeException("User was not found");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
    }
}