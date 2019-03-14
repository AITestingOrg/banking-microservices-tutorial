'#!/bin/sh'
./gradlew :transactions:test --tests "*.contract.consumer.*"
./gradlew :customers:pactVerify
./gradlew :customers:pactPublish