#!/usr/bin/env bash
./gradlew test -x :integration-tests:test -x :account-cmd:test --tests "*.service.isolation.*"
