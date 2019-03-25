@echo off
./gradlew.bat test -x :integration-tests:test -x :account-cmd:test --tests "*.service.isolation.*"