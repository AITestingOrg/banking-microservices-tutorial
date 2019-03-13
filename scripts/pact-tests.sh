'#!/bin/sh'
../gradlew cleanTest :transactions:test --tests "*.contract.consumer.*"
../gradlew :customers:pactVerify
../gradlew :customers:pactPublish