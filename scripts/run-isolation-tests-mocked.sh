#!/usr/bin/env bash
export MICRONAUT_ENVIRONMENTS=test,external_mocks
./gradlew test -x :tests:integration-tests:test -x :domain-services:account-cmd:test --tests "*.service.isolation.*" --info
