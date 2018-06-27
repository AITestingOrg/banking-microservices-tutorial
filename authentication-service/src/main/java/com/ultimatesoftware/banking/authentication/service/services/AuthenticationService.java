package com.ultimatesoftware.banking.authentication.service.services;

import com.ultimatesoftware.banking.authentication.service.model.User;
import com.ultimatesoftware.banking.authentication.service.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JWTGenerator generator;

    public ResponseEntity registerUser(User creds) {
        if (creds.getUserName() == null || creds.getUserName().isEmpty()) {
            throw new BadCredentialsException("userName parameter required");
        }
        if (creds.getPassword() == null || creds.getPassword().isEmpty()) {
            throw new BadCredentialsException("userName parameter required");
        }
        User user = new User(creds.getUserName(), creds.getPassword());
        user.setPassword(bCryptPasswordEncoder.encode(creds.getPassword()));
        userRepository.save(user);

        String account = "{\n \"id\": \"" + user.getId() + "\",\n\"userName\": \""
                + user.getUserName() + "\",\n\"password\": \"insertYourPassword\"\n}";
        return new ResponseEntity<>(account, HttpStatus.ACCEPTED);
    }

    public ResponseEntity loginUser(User userObj) {

        if (isUserValid(userObj)) {
            return new ResponseEntity<>(generator.generate(getUserById(userObj.getId())), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("User Not Found", HttpStatus.ACCEPTED);
    }

    private User getUserById(UUID id) {
        return userRepository.findOne(id);
    }

    private boolean isUserValid(User userObj) {

        User dbUser = getUserById(userObj.getId());
        return bCryptPasswordEncoder.matches(userObj.getPassword(), dbUser.getPassword())
                && userObj.getId().equals(dbUser.getId())
                && userObj.getPassword().equalsIgnoreCase(dbUser.getPassword())
                && userObj.getUserName().equalsIgnoreCase(dbUser.getUserName());

    }






}
