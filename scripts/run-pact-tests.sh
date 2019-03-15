'#!/bin/sh'
./gradlew :customers:pactVerify_AccountCmd
./gradlew :customers:pactVerify_AccountQuery
./gradlew :customers:pactVerify_Customers
./gradlew :customers:pactVerify_Transactions