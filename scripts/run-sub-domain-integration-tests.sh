#!/usr/bin/env bash
'#!/bin/sh'
./gradlew cleanTest :integration-tests:test --tests "*.subdomain.*"
