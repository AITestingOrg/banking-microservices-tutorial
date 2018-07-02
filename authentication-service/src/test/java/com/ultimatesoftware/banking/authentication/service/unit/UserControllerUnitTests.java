package com.ultimatesoftware.banking.authentication.service.unit;

import com.ultimatesoftware.banking.authentication.service.controllers.UserController;
import com.ultimatesoftware.banking.authentication.service.services.AuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.ultimatesoftware.banking.authentication.service.helpers.TestConstants.T_USER;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerUnitTests {


    @Mock
    private AuthenticationService service;
    @InjectMocks
    private UserController controller;



    @Test
    public void register_UserAsParam_ServiceRegisterUser() {
        // arrange

        // act
        controller.register(T_USER);

        // assert
        verify(service, times(1)).registerUser(T_USER);
    }

    @Test
    public void login_UserAsParam_ServiceLoginUser() {
        // arrange

        // act
        controller.login(T_USER);

        // assert
        verify(service, times(1)).loginUser(T_USER);
    }
}
