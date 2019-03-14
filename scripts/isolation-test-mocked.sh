#!/bin/sh
export MICRONAUT_ENVIRONMENTS=test,mock
./gradlew :customers:test --tests "*.isolated.mocked.*"