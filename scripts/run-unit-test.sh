'#!/bin/sh'
./gradlew test --tests "*.unit.*"
./gradlew jacocoTestCoverageVerification
./gradlew jacocoTestReport