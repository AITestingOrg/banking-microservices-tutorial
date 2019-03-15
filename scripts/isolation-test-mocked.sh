#!/bin/sh
export MICRONAUT_ENVIRONMENTS=test,external_mocks
./gradlew :customers:test --tests "*.isolated.*"