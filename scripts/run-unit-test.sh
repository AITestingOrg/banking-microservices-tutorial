'#!/bin/sh'
./gradlew test --tests "*.unit.*"
./gradlew :transactions:jacocoTestCoverageVerification
./gradlew :transactions:jacocoTestReport