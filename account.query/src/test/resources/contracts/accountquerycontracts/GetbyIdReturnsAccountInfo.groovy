package accountquerycontracts

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description("When a GET request to api/v1/accounts/{ID} with valid id should return 200")
        request {
            method 'GET'
            url '/api/v1/accounts/7a7d1e99-4823-4aa5-9d3b-2307e88cee08'
            headers{
                contentType(applicationJson())
            }
        }
        response {
            status 200
            body(
                    "id": "7a7d1e99-4823-4aa5-9d3b-2307e88cee08",
                    "customerId": "f6e0ef7e-93af-47e0-b665-e9fbdc184b43",
                    "balance": 0.0
            )
            headers {
                contentType(applicationJson())
            }
        }
    }
]
