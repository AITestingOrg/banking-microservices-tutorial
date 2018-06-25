package com.ultimatesoftware.banking.authorization.service.User;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private ServiceUserRepository serviceUserRepository;


    public UserDetailsServiceImpl(ServiceUserRepository serviceUserRepository) {
        this.serviceUserRepository = serviceUserRepository;
    }
    public UserDetailsServiceImpl(){}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServiceUser serviceUser = serviceUserRepository.findByUsername(username);
        if (serviceUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(serviceUser.getUsername(), serviceUser.getPassword(), emptyList());
    }
}
