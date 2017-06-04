#!/bin/bash
set -ev
if [ "${BUILD_TYPE}" = "ORACLE_JDK_8" ]; then
    ./jdk_switcher.sh use oraclejdk8
    ./gradlew test
fi

if [ "${BUILD_TYPE}" = "OPEN_JDK_8" ]; then
    ./jdk_switcher.sh use openjdk8
    ./gradlew test
fi
