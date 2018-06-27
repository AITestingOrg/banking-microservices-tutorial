package com.ultimatesoftware.banking.authentication.service.controllers;

import com.ultimatesoftware.banking.authentication.service.model.User;
import com.ultimatesoftware.banking.authentication.service.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/auth")
public class UserController {

    @Autowired
    private AuthenticationService service;

    @PostMapping(value = "register")
    public ResponseEntity register(@RequestBody User user) {
        return service.registerUser(user);
    }

    @PostMapping(value = "login")
    public ResponseEntity login(@RequestBody User user) {
        return service.loginUser(user);
    }

    @GetMapping(value = "test")
    public String hello() {
        return "Hello World";
    }
}
