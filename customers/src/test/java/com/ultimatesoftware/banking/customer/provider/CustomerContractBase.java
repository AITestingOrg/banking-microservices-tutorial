package com.ultimatesoftware.banking.customer.provider;

import com.ultimatesoftware.banking.customer.CustomerApplication;
import com.ultimatesoftware.banking.customer.domain.models.Customer;
import com.ultimatesoftware.banking.customer.service.repositories.CustomerRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = CustomerApplication.class)
public abstract class CustomerContractBase {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CustomerRepository customerRepository;


    private final String id1 = "12093be5-03a7-43a1-a892-a3f614bc6564";
    private final String id2 = "13147684-7d55-476c-ae11-8383407a7f13";
    private final Customer customer = new Customer(id1, "FirstName1", "LastName1");
    private final Customer customer2 = new Customer(id2, "FirstName2", "LastName2");


    @Before
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        customerRepository.save(customer);
        customerRepository.save(customer2);
    }

    @After
    public void teardown() {
    }
}

