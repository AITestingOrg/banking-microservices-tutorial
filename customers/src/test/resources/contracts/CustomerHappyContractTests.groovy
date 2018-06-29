import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        description("When a GET request to api/v1/customers return 200 and list of customers")
        request {
            method 'GET'
            url 'api/v1/customers'
            headers{
                contentType(applicationJson())
            }
        }
        response {
            status 200
            body("""
                [
                  {
                    "id": "12093be5-03a7-43a1-a892-a3f614bc6564",
                    "firstName": "FirstName1",
                    "lastName": "LastName1"
                  },
                  {
                    "id": "13147684-7d55-476c-ae11-8383407a7f13",
                    "firstName": "FirstName2",
                    "lastName": "LastName2"
                  }
                ]
                """
            )
            headers {
                contentType(applicationJson())
            }
        }
    },
    Contract.make {
        description("When a GET request to api/v1/customers/{id} with valid id return 200 and customer")
        request {
            method 'GET'
            url 'api/v1/customers/13147684-7d55-476c-ae11-8383407a7f13'
            headers{
                contentType(applicationJson())
            }
        }
        response {
            status 200
            body(
                    "id": "13147684-7d55-476c-ae11-8383407a7f13",
                    "firstName": "FirstName2",
                    "lastName": "LastName2"
            )
            headers {
                contentType(applicationJson())
            }
        }
    },
    Contract.make {
        description("When a POST request to api/v1/customers with valid customerBody return 200 and id")
        request {
            method 'POST'
            url 'api/v1/customers'
            headers {
                contentType(applicationJson())
            }
            body(
                    "id": "13147684-7d55-476c-ae11-8383407a7f13",
                    "firstName": "FirstName2",
                    "lastName": "LastName2"
            )
        }
        response {
            status 200
            headers {
                contentType(textPlain())
            }

            body(
                $("13147684-7d55-476c-ae11-8383407a7f13")
            )
        }
    },
    Contract.make {
        description("When a PUT request to api/v1/customers/{id} with valid customer body and id return 200")
        request {
            method 'PUT'
            url 'api/v1/customers/13147684-7d55-476c-ae11-8383407a7f13'
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
            status 200
        }
    },
    Contract.make {
        description("When a DELETE request to api/v1/customers/{id} with valid id return 200")
        request {
            method 'DELETE'
            url 'api/v1/customers/13147684-7d55-476c-ae11-8383407a7f13'
        }
        response {
            status 200
        }
    }
]
