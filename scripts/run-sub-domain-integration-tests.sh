#!/usr/bin/env bash
./gradlew cleanTest :integration-tests:test --tests "*.subdomain.*"
