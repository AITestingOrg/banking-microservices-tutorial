package com.ultimatesoftware.banking.authorization.service.controllers;

import com.ultimatesoftware.banking.authorization.service.model.User;
import com.ultimatesoftware.banking.authorization.service.security.JWTGenerator;
import com.ultimatesoftware.banking.authorization.service.services.ServiceUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value="api/v1/auth")
public class UserController {

    @Autowired
    private ServiceUserRepository serviceUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JWTGenerator generator;

    public UserController(ServiceUserRepository serviceUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          JWTGenerator generator) {
        this.serviceUserRepository = serviceUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.generator = generator;
    }

    @PostMapping(value="register")
    public ResponseEntity register(@RequestBody User userObj) {
        if(userObj.getUserName() == null || userObj.getUserName().isEmpty()) {
            throw new BadCredentialsException("userName parameter required");}
        if(userObj.getPassword() == null || userObj.getPassword().isEmpty()) {
            throw new BadCredentialsException("userName parameter required");}
        User user = new User(userObj.getUserName(),userObj.getPassword());
        user.setPassword(bCryptPasswordEncoder.encode(userObj.getPassword()));
        serviceUserRepository.save(user);
        User useract = new User(userObj.getUserName(),userObj.getPassword());
        serviceUserRepository.save(useract);

        String account = "{ \"id\": \"" + useract.getId() + "\",\n\"userName\": \"" + useract.getUserName()
                + "\",\n\"password\": insertYourPassword\n}";

        return new ResponseEntity<>(account, HttpStatus.ACCEPTED);
    }

    @PostMapping(value="login")
    public ResponseEntity login(@RequestBody User userObj) {

       User userRecord = serviceUserRepository.findOne(userObj.getId());
        if(bCryptPasswordEncoder.matches(userObj.getPassword(), userRecord.getPassword())) {
            return new ResponseEntity<>(generator.generate(userRecord), HttpStatus.ACCEPTED);
        }
        if(userObj.getId().equals(userRecord.getId()) && userObj.getPassword().equalsIgnoreCase(userRecord.getPassword()) && userObj.getUserName().equalsIgnoreCase(userRecord.getUserName())) {
            return new ResponseEntity<>(generator.generate(userRecord), HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("User Not Found", HttpStatus.ACCEPTED);
    }

    @GetMapping(value="test")
    public String hello() {
        return "Hello World";
    }
}
