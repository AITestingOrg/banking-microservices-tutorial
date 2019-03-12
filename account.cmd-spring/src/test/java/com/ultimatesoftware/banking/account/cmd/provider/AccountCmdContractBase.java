package com.ultimatesoftware.banking.account.cmd.provider;

import com.ultimatesoftware.banking.account.cmd.AccountCmdApplication;
import com.ultimatesoftware.banking.account.cmd.domain.models.AccountCreationDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = AccountCmdApplication.class)
public abstract class AccountCmdContractBase {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @After
    public void teardown() {
    }
}
