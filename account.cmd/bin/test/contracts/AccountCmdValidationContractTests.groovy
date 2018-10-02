import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description("When a POST request to api/v1/accounts with a invalid UUID should return 400")
        request {
            method 'POST'
            url '/api/v1/accounts'
            headers{
                contentType(applicationJson())
            }
            body(
                    "customerId":"123e4567-e89b-12d3-a456-4266554400"
            )
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/accounts/{id} with a invalid ID should return 400")
        request {
            method 'PUT'
            url '/api/v1/accounts/f6e0ef7e-93af'
            headers{
                contentType(applicationJson())
            }
            body(
                    "customerId": "f6e0ef7e-93af-47e0-b665-e9fbdc184b43",
            )
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/accounts/{id} with no body should return 400")
        request {
            method 'PUT'
            url '/api/v1/accounts/13147684-7d55-476c-ae11-8383407a7f13'
            headers{
                contentType(applicationJson())
            }
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/accounts/{id} with invalid body should return 400")
        request {
            method 'PUT'
            url '/api/v1/accounts/13147684-7d55-476c-ae11-8383407a7f13'
            headers{
                contentType(applicationJson())
            }
            body(
                    "customerId": "f6e0ef7e-93af-47e0-b665",
            )
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/accounts/credit with a transaction body missing ID should return 400")
        request {
            method 'PUT'
            url '/api/v1/accounts/credit'
            headers{
                contentType(applicationJson())
            }
            body(
                    "account": "13147684-7d55-476c-ae11-8383407a7f13",
                    "customerId": "f6e0ef7e-93af-47e0-b665-e9fbdc184b43",
                    "amount": 300
            )
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/accounts/credit with a transaction body missing account should return 400")
        request {
            method 'PUT'
            url '/api/v1/accounts/credit'
            headers{
                contentType(applicationJson())
            }
            body(
                    "id": "9e7a4e7f-4685-4908-bcfa-0c78d25d1b6b",
                    "customerId": "f6e0ef7e-93af-47e0-b665-e9fbdc184b43",
                    "amount": 300
            )
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/accounts/credit with a transaction body missing customerId should return 400")
        request {
            method 'PUT'
            url '/api/v1/accounts/credit'
            headers{
                contentType(applicationJson())
            }
            body(
                    "id": "9e7a4e7f-4685-4908-bcfa-0c78d25d1b6b",
                    "account": "13147684-7d55-476c-ae11-8383407a7f13",
                    "amount": 300
            )
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/accounts/credit with a transaction body missing amount should return 400")
        request {
            method 'PUT'
            url '/api/v1/accounts/credit'
            headers{
                contentType(applicationJson())
            }
            body(
                    "id": "9e7a4e7f-4685-4908-bcfa-0c78d25d1b6b",
                    "account": "13147684-7d55-476c-ae11-8383407a7f13",
                    "customerId": "f6e0ef7e-93af-47e0-b665-e9fbdc184b43"
            )
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/accounts/debit with a transaction body missing ID should return 400")
        request {
            method 'PUT'
            url '/api/v1/accounts/debit'
            headers{
                contentType(applicationJson())
            }
            body(
                    "account": "13147684-7d55-476c-ae11-8383407a7f13",
                    "customerId": "f6e0ef7e-93af-47e0-b665-e9fbdc184b43",
                    "amount": 300
            )
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/accounts/debit with a transaction body missing account should return 400")
        request {
            method 'PUT'
            url '/api/v1/accounts/debit'
            headers{
                contentType(applicationJson())
            }
            body(
                    "id": "9e7a4e7f-4685-4908-bcfa-0c78d25d1b6b",
                    "customerId": "f6e0ef7e-93af-47e0-b665-e9fbdc184b43",
                    "amount": 300
            )
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/accounts/debit with a transaction body missing customerId should return 400")
        request {
            method 'PUT'
            url '/api/v1/accounts/debit'
            headers{
                contentType(applicationJson())
            }
            body(
                    "id": "9e7a4e7f-4685-4908-bcfa-0c78d25d1b6b",
                    "account": "13147684-7d55-476c-ae11-8383407a7f13",
                    "amount": 300
            )
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/accounts/debit with a transaction body missing amount should return 400")
        request {
            method 'PUT'
            url '/api/v1/accounts/debit'
            headers{
                contentType(applicationJson())
            }
            body(
                    "id": "9e7a4e7f-4685-4908-bcfa-0c78d25d1b6b",
                    "account": "13147684-7d55-476c-ae11-8383407a7f13",
                    "customerId": "f6e0ef7e-93af-47e0-b665-e9fbdc184b43"
            )
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a DELETE request to api/v1/{ID} with a invalid UUID return 400")
        request {
            method 'DELETE'
            url '/api/v1/accounts/123e4567-e89b-12d3-a456'
        }
        response {
            status 400
        }
    },
    Contract.make {
        description("When a PUT request to /api/v1/accounts/transfer with a valid transaction body should return 200")
        request {
            method 'PUT'
            url '/api/v1/accounts/transfer'
            headers{
                contentType(applicationJson())
            }
            body(
                "id": "9e7a4e7f-4685-4908-bcfa-0c78d25d1b6b",
                "account": "13147684-7d55-476c-ae11-8383407a7f13",
                "customerId": "f6e0ef7e-93af-47e0-b665-e9fbdc184b43",
                "destinationAccount": "f6e0ef7e-93af-47e0-b665-e9fbdc184b43",
                "amount": 300
            )
        }
        response {
            status 200
        }
    },
]

