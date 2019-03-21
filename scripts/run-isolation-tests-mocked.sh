#!/bin/sh
export MICRONAUT_ENVIRONMENTS=test,external_mocks
./gradlew test --tests "*.service.isolation.*"