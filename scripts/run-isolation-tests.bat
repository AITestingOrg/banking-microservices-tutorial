@echo off
./gradlew.bat test -x :tests:integration-tests:test -x :domain-services:account-cmd:test --tests "*.service.isolation.*"