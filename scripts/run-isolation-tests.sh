#!/usr/bin/env bash
./gradlew test -x :tests:integration-tests:test --tests "*.service.isolation.*"
