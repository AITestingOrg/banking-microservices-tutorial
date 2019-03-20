@echo off
./gradlew test --tests "*.unit.*"
./gradlew jacocoTestCoverageVerification
./gradlew jacocoTestReport