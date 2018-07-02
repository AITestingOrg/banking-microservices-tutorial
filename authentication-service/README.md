# Authentication-Service 
Resquires authetication to access all endpoints except for the REGISTER and LOGIN endpoints
that are used to generate a token.


## Features
* Create an account (Register)
* Login to receive token

# Demo
To run this project from gradle:
    - MongoDb must be running
    - From command line run 'gradle bootrun'

## Endpoints

#### POST to api/v1/auth/register

With json body:

```
{
	"userName": "admin",
	"password": "admin"
}
```

This method will return the generated ID for the account and a template of the JSON payload that should be sent in the login request.

#### POST to api/v1/auth/login
* Submit registered credentials to revieve JWT.

```
{
	"userName": "admin",
	"password": "admin"
}
```

Copy the JWT for use to access resources.

#### Test your token GET to api/v1/auth/test
*  Modify the headers of your request and be sure to prefix your token with Bearer(SPACE) as seen below.
    - Header = "Authorization"
        - Value = "Bearer  INSERTTOKENVALUE"

# Development

## Requirements
* Docker 17.xx.x
* JDK 1.8
* IntelliJ 2018
* MongoDB


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

#### Prerequisites

The following applications must have been installed in your system previous to run this application.

 - Java8
 - MongoDB
 

## Built With

* [Spring Boot](https://spring.io/docs) - Application Framework
* [Gradle](https://docs.gradle.org/4.2/release-notes.html) - Dependency Management

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

Passenger