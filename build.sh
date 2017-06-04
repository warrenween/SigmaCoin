#!/bin/bash
set -ev
if [ "${BUILD_TYPE}" = "ORACLE_JDK_8" ]; then
    sudo apt-get -y --force-yes install oracle-java8-installer
    ./jdk_switcher.sh use oraclejdk8
    java -version
    javac -version
    ./gradlew test
fi

if [ "${BUILD_TYPE}" = "OPEN_JDK_8" ]; then
    sudo apt-get install openjdk-8-jdk
    ./jdk_switcher.sh use openjdk8
    java -version
    javac -version
    ./gradlew test
fi
