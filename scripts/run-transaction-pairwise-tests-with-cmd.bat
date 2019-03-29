@echo off
./gradlew.bat cleanTest :tests:integration-tests:test --tests "*.transactions.pairwise.cmd.*"
