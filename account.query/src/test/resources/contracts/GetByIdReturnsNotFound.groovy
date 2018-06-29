import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description("When a GET request to api/v1/accounts/{ID} with non existing id should return 404")
        request {
            method 'GET'
            url '/api/v1/accounts/f6e0ef7e-93af-47e0-b665-e9fbdc184b43'
            headers{
                contentType(applicationJson())
            }
        }
        response {
            status 404
        }
    }
]
