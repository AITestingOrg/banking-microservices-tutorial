@echo off
./gradlew.bat test -x :tests:integration-tests:test --tests "*.unit.*" -i
./gradlew.bat jacocoTestCoverageVerification
./gradlew.bat jacocoTestReport
