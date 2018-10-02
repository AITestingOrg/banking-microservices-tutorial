import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description("When a POST request to api/v1/accounts with a valid UUID should return 200 and the new UUID")
        request {
            method 'POST'
            url '/api/v1/accounts'
            headers{
                contentType(applicationJson())
            }
            body(
                    "customerId":"123e4567-e89b-12d3-a456-426655440000"
            )
        }
        response {
            status 200
            body(
                $(regex("\"[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}\""))
            )
            headers {
                contentType(applicationJson())
            }
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/accounts/credit with a valid transaction body should return 200")
        request {
            method 'PUT'
            url '/api/v1/accounts/credit'
            headers{
                contentType(applicationJson())
            }
            body(
                "id": "9e7a4e7f-4685-4908-bcfa-0c78d25d1b6b",
                "account": "13147684-7d55-476c-ae11-8383407a7f13",
                "customerId": "f6e0ef7e-93af-47e0-b665-e9fbdc184b43",
                "amount": 300
            )
        }
        response {
            status 200
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/accounts/debit with a valid transaction body should return 200")
        request {
            method 'PUT'
            url '/api/v1/accounts/debit'
            headers{
                contentType(applicationJson())
            }
            body(
                "id": "9e7a4e7f-4685-4908-bcfa-0c78d25d1b6b",
                "account": "13147684-7d55-476c-ae11-8383407a7f13",
                "customerId": "f6e0ef7e-93af-47e0-b665-e9fbdc184b43",
                "amount": 300
            )
        }
        response {
            status 200
        }
    },
    Contract.make {
        description("When a DELETE request to api/v1/{ID} with a valid UUID return 200")
        request {
            method 'DELETE'
            url '/api/v1/accounts/123e4567-e89b-12d3-a456-426655440000'
        }
        response {
            status 200
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
