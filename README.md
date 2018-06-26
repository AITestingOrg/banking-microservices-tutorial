# Banking Microservices
[![Build Status](https://travis-ci.org/AITestingOrg/banking-microservices-example.svg?branch=master)](https://travis-ci.org/AITestingOrg/banking-microservices-example)
## Configuration
All services are built with Spring Boot, the configuration files are under /src/main/resources/application.properties

## Requirements
See each services readme for detailed requirement information

### Compose
* https://docs.docker.com/compose/install/
* /data/db directory created and accessible to "everyone"

## Start the microservices
** Build JARs for each project (You will need to build a JAR anytime changes are made to a project, then rebuild either the container or all containers)
```bash
gradle assemble
docker-compose up
```

## Stop the Containers
```bash
docker-compose down
```

## Rebuild Containers
```bash
docker-compose build
```

## Running with Centralized Logging (ELK stack)
To run with centralized logging and logging visualizations follow the steps below.
### Start the ELK stack
* `cd elk`
* `docker-compose up` wait for everything to start
* Check that http://localhost:5601 is accessible in your browser, you can read about configuration Kibana here https://www.elastic.co/guide/en/kibana/4.0/setup.html
* `cd ../`
* `docker-compose up -f docker-compose-elk.yml`
* Refresh Kibana to see the logs.

