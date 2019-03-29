#!/usr/bin/env bash
./gradlew test -x :tests:integration-tests:test --tests "*.unit.*" -i
./gradlew jacocoTestCoverageVerification
./gradlew jacocoTestReport
