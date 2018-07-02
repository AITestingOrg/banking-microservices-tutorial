package com.ultimatesoftware.banking.authentication.service.services;

import com.ultimatesoftware.banking.authentication.service.model.User;
import com.ultimatesoftware.banking.authentication.service.repository.UserRepository;
import com.ultimatesoftware.banking.authentication.service.repository.UserRepositoryImpl;
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
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;


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

        String account = "Account id: " + user.getId() + "\n{\n\"userName\": \""
                + user.getUserName() + "\",\n\"password\": \"insertYourPassword\"\n}";
        return new ResponseEntity<>(account, HttpStatus.ACCEPTED);
    }

    public ResponseEntity loginUser(User userObj) {

        if (isUserNamePassValid(userObj)) {
            return new ResponseEntity<>(generator.generate(userRepositoryImpl
                    .findByUserName(userObj.getUserName())), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }
    }


    public boolean isUserNamePassValid(User userObj) {

        User dbUser = findByUserName(userObj.getUserName());
        return bCryptPasswordEncoder.matches(userObj.getPassword(), dbUser.getPassword())
                && userObj.getUserName().equalsIgnoreCase(dbUser.getUserName());
    }

    private User findByUserName(String userName) {
       return userRepositoryImpl.findByUserName(userName);
    }

}
