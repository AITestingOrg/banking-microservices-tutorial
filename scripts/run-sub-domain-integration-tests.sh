#!/usr/bin/env bash
./gradlew cleanTest :tests:integration-tests:test --tests "*.subdomain.*"
