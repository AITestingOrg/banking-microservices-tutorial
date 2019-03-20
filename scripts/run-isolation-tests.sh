#!/bin/sh
./gradlew :transactions:run
./gradlew :transactions:test --tests "*.isolation.*"