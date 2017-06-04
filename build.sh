#!/bin/bash
set -ev
if [ "${BUILD_TYPE}" = "JDK8" ]; then
    sudo apt-get -y --force-yes install oracle-java8-installer
    ./gradlew test
fi

if [ "${BUILD_TYPE}" = "OPENJDK8" ]; then
    sudo apt-get -y --force-yes install java-1.8.0-openjdk
    ./gradlew test
fi
