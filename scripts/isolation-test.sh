#!/bin/sh
export MICRONAUT_ENVIRONMENTS=test
./gradlew :customers:test --tests "*.isolated.*"