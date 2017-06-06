#!/bin/bash
set -ev
if [ "${TRAVIS_BRANCH}" = "book" ]; then
    pandoc --version
fi

