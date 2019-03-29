#!/usr/bin/env bash

# Generate the Pact JSON files in the root of the project in 'pact-tests'.
# To change the target directory see `systemProperties['pact.rootDir'] = "../pact-tests"`
# An example change could be `systemProperties['pact.rootDir'] = "${buildDir}/pact-tests"` to
# store them in each projects directory, but then the publish location needs to be created for each
# provider.
./gradlew :account-cmd:test --tests "*.contracts.consumer.*"
./gradlew :account-query:test --tests "*.contracts.consumer.*"
./gradlew :people-details:test --tests "*.contracts.consumer.*"
./gradlew :account-transactions:test --tests "*.contracts.consumer.*"

# Since the PACT plugin is global to all projects this will publish all provider pacts, not just
# transactions.
./gradlew :account-transactions:pactPublish
