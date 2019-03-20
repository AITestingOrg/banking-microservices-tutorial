@echo off
./gradlew :transactions:test --tests "*.isolation.*"