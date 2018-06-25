package com.ultimatesoftware.banking.authorization.service.security;

import com.ultimatesoftware.banking.authorization.service.User.ServiceUserRepository;
import com.ultimatesoftware.banking.authorization.service.model.JwtAuthenticationToken;
import com.ultimatesoftware.banking.authorization.service.model.User;
import com.ultimatesoftware.banking.authorization.service.model.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//this is authentication
@Component
public class JWTAuthorizationFilter extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private JwtValidator validator;

    @Autowired
    private ServiceUserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
//
//        if(token.getCredentials() == null
//                || userDetails.getPassword() == null) {
//            throw new BadCredentialsException("Credentials may not be null.");
//        }
//
//        if(!encoder.matches((String) token.getCredentials(),userDetails.getPassword())) {
//            throw new BadCredentialsException("Invalid Password!");
//        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken;
        String token = jwtAuthenticationToken.getToken();

        User user = validator.validate(token);

        if(user == null) {
            throw new RuntimeException("JWT Token is incorrect");
        }
        //else if ()

      //  if(userRepository.exists(user.getId())) {
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                    .commaSeparatedStringToAuthorityList(user.getRole());
            return new JwtUserDetails(user.getUserName(), user.getId(),
                    token,
                    grantedAuthorities);
      //  }
     //   else throw new RuntimeException("User was not found");
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
    }
}