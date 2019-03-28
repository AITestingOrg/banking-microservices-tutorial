@echo off
./gradlew.bat test --tests "*.unit.*"
./gradlew.bat jacocoTestCoverageVerification
./gradlew.bat jacocoTestReport