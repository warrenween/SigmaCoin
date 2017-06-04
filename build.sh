#!/bin/bash
set -ev
if [ "${BUILD_TYPE}" = "JDK8" ]; then
    sudo jdk_switcher use oraclejdk8
    ./gradlew test
fi

if [ "${BUILD_TYPE}" = "OPENJDK8" ]; then
    sudo jdk_switcher use openjdk8
    ./gradlew test
fi
