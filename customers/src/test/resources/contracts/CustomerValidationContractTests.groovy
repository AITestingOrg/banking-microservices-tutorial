import org.springframework.cloud.contract.spec.Contract

[

        Contract.make {
            description("When a GET request to api/v1/customers/{id} with valid id that does not exist return 404")
            request {
                method 'GET'
                url 'api/v1/customers/f6e0ef7e-93af-47e0-b665-e9fbdc184b43'
                headers{
                    contentType(applicationJson())
                }
            }
            response {
                status 404
            }
        },
        Contract.make {
            description("When a POST request to api/v1/customers with invalid body return 400")
            request {
                method 'POST'
                url 'api/v1/customers'
                headers {
                    contentType(applicationJson())
                }
                body(
                        "firstName": "FirstName2",
                        "lastName": "LastName2"
                )
            }
            response {
                status 400
            }
        },
        Contract.make {
            description("When a POST request to api/v1/customers with invalid body return 400")
            request {
                method 'POST'
                url 'api/v1/customers'
                headers {
                    contentType(applicationJson())
                }
                body(
                        "id": "13147684-7d55-476c-ae11",
                        "lastName": "LastName2"
                )
            }
            response {
                status 400
            }
        },
        Contract.make {
            description("When a POST request to api/v1/customers with invalid body return 400")
            request {
                method 'POST'
                url 'api/v1/customers'
                headers {
                    contentType(applicationJson())
                }
                body(
                        "id": "13147684-7d55-476c-ae11",
                        "firstName": "FirstName2",
                )
            }
            response {
                status 400
            }
        },
        Contract.make {
            description("When a PUT request to api/v1/customers/{id} with non existing id return 404")
            request {
                method 'PUT'
                url 'api/v1/customers/13147684-7d55-ae11-8383407a7f13'
                headers{
                    contentType(applicationJson())
                }
                body(
                        "id": "13147684-7d55-476c-ae11-8383407a7f13",
                        "firstName": "FirstName2",
                        "lastName": "LastName2"
                )
            }
            response {
                status 404
            }
        },
        Contract.make {
            description("When a PUT request to api/v1/customers/{id} with id that does not exist return 404")
            request {
                method 'PUT'
                url 'api/v1/customers/f6e0ef7e-93af-47e0-b665-e9fbdc184b43'
                headers{
                    contentType(applicationJson())
                }
                body(
                        "id": "13147684-7d55-476c-ae11-8383407a7f13",
                        "firstName": "FirstName2",
                        "lastName": "LastName2"
                )
            }
            response {
                status 404
            }
        },
        Contract.make {
            description("When a DELETE request to api/v1/customers/{id} with id that doesn't exist return 404")
            request {
                method 'DELETE'
                url 'api/v1/customers/f6e0ef7e-93af-47e0-b665-e9fbdc184b43'
            }
            response {
                status 404
            }
        }
]

