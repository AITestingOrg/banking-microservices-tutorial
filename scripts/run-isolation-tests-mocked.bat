@echo off
$env:MICRONAUT_ENVIRONMENTS = "test,external_mocks"
./gradlew.bat test --tests "*.service.isolation.*"