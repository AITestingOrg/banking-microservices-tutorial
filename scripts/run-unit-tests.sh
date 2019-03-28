#!/usr/bin/env bash
'#!/bin/sh'
./gradlew test -x :integration-tests:test --tests "*.unit.*"
./gradlew jacocoTestCoverageVerification
./gradlew jacocoTestReport
