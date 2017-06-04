#!/bin/bash
set -ev
if [ "${BUILD_TYPE}" = "ORACLE_JDK_8" ]; then
    sudo add-apt-repository ppa:webupd8team/java -y
    sudo apt-get update
    sudo apt-get install oracle-java8-installer
    sudo apt install oracle-java8-set-default
    ./jdk_switcher.sh use oraclejdk8
    java -version
    javac -version
    ./gradlew test
fi

if [ "${BUILD_TYPE}" = "OPEN_JDK_8" ]; then
    sudo add-apt-repository ppa:openjdk-r/ppa -y
    sudo apt-get update
    sudo apt-get install openjdk-8-jdk
    #./jdk_switcher.sh use openjdk8
    java -version
    javac -version
    ./gradlew test
fi
