import org.springframework.cloud.contract.spec.Contract
[
        Contract.make {
            description("When a GET request to api/v1/accounts/{ID} with valid id that does not exist should return 404")
            request {
                method 'GET'
                url '/api/query/accounts/7a7d1e99-4823-4aa5-9d3b-2307e88cee08'
                headers{
                    contentType(applicationJson())
                }
            }
            response {
                status 404
                headers {
                    contentType(applicationJson())
                }
            }
        }
] 