@echo off
$env:MICRONAUT_ENVIRONMENTS = "test,external_mocks"
./gradlew.bat test -x :integration-tests:test -x :account-cmd:test --tests "*.service.isolation.*"