# Banking Microservices

## Configuration
All services are built with Spring Boot, the configuration files are under /src/main/resources/application.properties

## Requirements
See each services readme for detailed requirement information
### Compose
* https://docs.docker.com/compose/install/
* /data/db directory created and accessible to "everyone"

## Unleash the Swarm
** Build JARs for each project (You will need to build a JAR anytime changes are made to a project, then rebuild either the container or all containers)
```bash
docker-compose up
```

## Stop the Containers
```bash
docker-compose stop
```

## Rebuild Containers
```bash
docker-compose build
```
