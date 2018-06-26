package com.ultimatesoftware.banking.authentication.service.services;

import com.ultimatesoftware.banking.authentication.service.model.User;
import com.ultimatesoftware.banking.authentication.service.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JWTGenerator generator;

    public ResponseEntity createUser(User creds) {
        if (creds.getUserName() == null || creds.getUserName().isEmpty()) {
            throw new BadCredentialsException("userName parameter required");
        }
        if (creds.getPassword() == null || creds.getPassword().isEmpty()) {
            throw new BadCredentialsException("userName parameter required");
        }
        User user = new User(creds.getUserName(), creds.getPassword());
        user.setPassword(bCryptPasswordEncoder.encode(creds.getPassword()));
        userRepository.save(user);
        User userAcct = new User(creds.getUserName(), creds.getPassword());
        userRepository.save(userAcct);

        String account = "Login using:\n{ \"id\": \"" + userAcct.getId() + "\",\n\"userName\": \""
                + userAcct.getUserName() + "\",\n\"password\": \"insertYourPassword\"\n}";
        return new ResponseEntity<>(account, HttpStatus.ACCEPTED);
    }

    public ResponseEntity authenticateUser(User userObj) {

        User dbUser = userRepository.findOne(userObj.getId());
        if (bCryptPasswordEncoder.matches(userObj.getPassword(), dbUser.getPassword())) {
            return new ResponseEntity<>(generator.generate(dbUser), HttpStatus.ACCEPTED);
        }
        if (userObj.getId().equals(dbUser.getId()) && userObj.getPassword().equalsIgnoreCase(dbUser.getPassword())
                && userObj.getUserName().equalsIgnoreCase(dbUser.getUserName())) {
            return new ResponseEntity<>(generator.generate(dbUser), HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("User Not Found", HttpStatus.ACCEPTED);
    }

    public boolean isUserValid(User userObj) {

        User dbUser = userRepository.findOne(userObj.getId());
        return bCryptPasswordEncoder.matches(userObj.getPassword(), dbUser.getPassword())
                && userObj.getId().equals(dbUser.getId())
                && userObj.getPassword().equalsIgnoreCase(dbUser.getPassword())
                && userObj.getUserName().equalsIgnoreCase(dbUser.getUserName());

    }



}
