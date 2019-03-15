import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description("When a GET request to api/v1/transactions/withdraw with an amount higher than balance returns 400")
        request {
            method 'GET'
            url 'api/v1/transactions/withdraw'
            headers{
                contentType(applicationJson())
                header "amount" : "500"
                header "accountId": "13147684-7d55-476c-ae11-8383407a7f13"
                header "customerId": "12093be5-03a7-43a1-a892-a3f614bc6564"
            }
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a GET request to api/v1/transactions/withdraw with an account that does not exist returns 400")
        request {
            method 'GET'
            url 'api/v1/transactions/withdraw'
            headers{
                contentType(applicationJson())
                header "amount" : "5"
                header "accountId": "12093be5-03a7-43a1-a892-a3f614bc6564"
                header "customerId": "12093be5-03a7-43a1-a892-a3f614bc6564"
            }
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a GET request to api/v1/transactions/withdraw with an customer that does not exist returns 400")
        request {
            method 'GET'
            url 'api/v1/transactions/withdraw'
            headers{
                contentType(applicationJson())
                header "amount" : "5"
                header "accountId": "13147684-7d55-476c-ae11-8383407a7f13"
                header "customerId": "a590a8dc-112d-45a3-856c-8bc52c9a2d5d"
            }
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a GET request to api/v1/transactions/deposit with an account that does not exist returns 400")
        request {
            method 'GET'
            url 'api/v1/transactions/deposit'
            headers{
                contentType(applicationJson())
                header "amount" : "5"
                header "accountId": "12093be5-03a7-43a1-a892-a3f614bc6564"
                header "customerId": "12093be5-03a7-43a1-a892-a3f614bc6564"
            }
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a GET request to api/v1/transactions/deposit with an customer that does not exist returns 400")
        request {
            method 'GET'
            url 'api/v1/transactions/deposit'
            headers{
                contentType(applicationJson())
                header "amount" : "5"
                header "accountId": "13147684-7d55-476c-ae11-8383407a7f13"
                header "customerId": "a590a8dc-112d-45a3-856c-8bc52c9a2d5d"
            }
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a GET request to api/v1/transactions/transfer with an amount higher than balance returns 400")
        request {
            method 'GET'
            url 'api/v1/transactions/transfer'
            headers{
                contentType(applicationJson())
                header "amount" : "500"
                header "accountId": "13147684-7d55-476c-ae11-8383407a7f13"
                header "customerId": "12093be5-03a7-43a1-a892-a3f614bc6564"
                header "destinationAccountId": "d99999ff-7324-4467-9468-44d40ca502a1"
            }
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a GET request to api/v1/transactions/transfer with an account that does not exist returns 400")
        request {
            method 'GET'
            url 'api/v1/transactions/transfer'
            headers{
                contentType(applicationJson())
                header "amount" : "5"
                header "accountId": "12093be5-03a7-43a1-a892-a3f614bc6564"
                header "customerId": "12093be5-03a7-43a1-a892-a3f614bc6564"
                header "destinationAccountId": "d99999ff-7324-4467-9468-44d40ca502a1"
            }
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a GET request to api/v1/transactions/transfer with an customer that does not exist returns 400")
        request {
            method 'GET'
            url 'api/v1/transactions/transfer'
            headers{
                contentType(applicationJson())
                header "amount" : "5"
                header "accountId": "13147684-7d55-476c-ae11-8383407a7f13"
                header "customerId": "a590a8dc-112d-45a3-856c-8bc52c9a2d5d"
                header "destinationAccountId": "d99999ff-7324-4467-9468-44d40ca502a1"
            }
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a GET request to api/v1/transactions/transfer with a destination that does not exist returns 400")
        request {
            method 'GET'
            url 'api/v1/transactions/transfer'
            headers{
                contentType(applicationJson())
                header "amount" : "5"
                header "accountId": "13147684-7d55-476c-ae11-8383407a7f13"
                header "customerId": "12093be5-03a7-43a1-a892-a3f614bc6564"
                header "destinationAccountId": "12093be5-03a7-43a1-a892-a3f614bc6564"
            }
        }
        response {
            status 400
        }
    },
]
