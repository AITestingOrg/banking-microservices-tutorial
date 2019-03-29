#!/usr/bin/env bash
./gradlew :tests:integration-tests:test --tests "*.transactions.pairwise.cmd.*"
