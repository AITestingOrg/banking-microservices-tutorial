#!/usr/bin/env bash
'#!/bin/sh'
./gradlew test -x :tests:integration-tests:test --tests "*.unit.*" -i
./gradlew jacocoTestCoverageVerification
./gradlew jacocoTestReport
