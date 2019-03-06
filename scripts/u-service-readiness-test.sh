'#!/bin/sh'
../gradlew cleanTest :transactions:test --tests "*.readiness.*"