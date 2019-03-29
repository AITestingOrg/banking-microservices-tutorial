@echo off
./gradlew.bat cleanTest :integration-tests:test --tests "*.transactions.pairwise.cmd.*"
