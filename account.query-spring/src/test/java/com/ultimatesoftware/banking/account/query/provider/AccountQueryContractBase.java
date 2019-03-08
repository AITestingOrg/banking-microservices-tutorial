package com.ultimatesoftware.banking.account.query.provider;

import com.ultimatesoftware.banking.account.query.Application;
import com.ultimatesoftware.banking.account.query.service.repositories.AccountRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static com.ultimatesoftware.banking.account.query.utils.TestConstants.ACCOUNT;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = Application.class)
public abstract class AccountQueryContractBase {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        accountRepository.save(ACCOUNT);
    }

    @After
    public void teardown() {
    }
}
