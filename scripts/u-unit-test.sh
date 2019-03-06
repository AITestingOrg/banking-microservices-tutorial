'#!/bin/sh'
../gradlew cleanTest :transactions:test --tests "*.unit.*"
../gradlew :transactions:jacocoTestCoverageVerification
../gradlew :transactions:jacocoTestReport