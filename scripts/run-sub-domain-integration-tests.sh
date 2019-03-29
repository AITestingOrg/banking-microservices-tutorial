#!/usr/bin/env bash
./gradlew :tests:integration-tests:test --tests "*.subdomain.*"
