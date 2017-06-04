#!/bin/bash
set -ev
if [ "${BUILD_TYPE}" = "JDK8" ]; then
    sudo apt-get -y --force-yes install oracle-java8-installer
    sudo apt-get -y --force-yes install oracle-java8-set-default
    ./gradlew test
fi

if [ "${BUILD_TYPE}" = "OPENJDK8" ]; then
    sudo apt-get -y --force-yes install openjdk-8-jdk
    ./gradlew test
fi
