package com.ultimatesoftware.banking.authorization.service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="authapi/v1/users")
public class ServiceUserController {

    private ServiceUserRepository serviceUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping
    public void register(@RequestBody ServiceUser serviceUser) {
        serviceUser.setPassword(bCryptPasswordEncoder.encode(serviceUser.getPassword()));
        serviceUserRepository.save(serviceUser);
    }
}
