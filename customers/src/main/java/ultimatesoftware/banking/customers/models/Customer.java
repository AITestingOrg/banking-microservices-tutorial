package ultimatesoftware.banking.customers.models;

import org.springframework.data.annotation.Id;

public class Customer {

    @Id
    public String id;
    public String firstName = "";
    public String lastName = "";

    public Customer(){
    }

    public Customer(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
