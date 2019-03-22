#!/bin/sh
export MICRONAUT_ENVIRONMENTS=test,external_mocks
./gradlew test -x :integration-tests:test -x :account-cmd:test --tests "*.service.isolation.*" --info