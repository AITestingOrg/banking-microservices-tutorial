
import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description("When a GET request to api/v1/transactions/withdraw with proper headers return 200 and transaction id")
        request {
            method 'GET'
            url 'api/v1/transactions/withdraw'
            headers{
                contentType(applicationJson())
                header "amount" : "5"
                header "accountId": "13147684-7d55-476c-ae11-8383407a7f13"
                header "customerId": "12093be5-03a7-43a1-a892-a3f614bc6564"
            }
        }
        response {
            status 200
            body(
                    $(regex("[a-fA-F0-9]*"))
            )
        }
    },
    Contract.make {
        description("When a GET request to api/v1/transactions/deposit with proper headers return 200 and transaction id")
        request {
            method 'GET'
            url 'api/v1/transactions/deposit'
            headers{
                contentType(applicationJson())
                header "amount" : "5"
                header "accountId": "13147684-7d55-476c-ae11-8383407a7f13"
                header "customerId": "12093be5-03a7-43a1-a892-a3f614bc6564"
            }
        }
        response {
            status 200
            body(
                    $(regex("[a-fA-F0-9]*"))
            )
        }
    },
    Contract.make {
        description("When a GET request to api/v1/transactions/transfer with proper headers return 200 and transaction id")
        request {
            method 'GET'
            url 'api/v1/transactions/transfer'
            headers{
                contentType(applicationJson())
                header "amount" : "5"
                header "accountId": "13147684-7d55-476c-ae11-8383407a7f13"
                header "customerId": "12093be5-03a7-43a1-a892-a3f614bc6564"
                header "destinationAccountId": "d99999ff-7324-4467-9468-44d40ca502a1"
            }
        }
        response {
            status 200
            body(
                    $(regex("[a-fA-F0-9]*"))
            )
        }
    }
]