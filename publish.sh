#!/bin/bash
set -ev

SOURCE_BRANCH="master"
TARGET_BRANCH="gh-pages"

function doCompile {
  ./compile.sh
}

if [ "$TRAVIS_PULL_REQUEST" != "false" -o "$TRAVIS_BRANCH" != "$SOURCE_BRANCH" ]; then
    echo "Skipping deploy; just doing a build."
    doCompile
    exit 0
fi

REPO=`git config remote.origin.url`
SSH_REPO=${REPO/https:\/\/github.com\//git@github.com:}
SHA=`git rev-parse --verify HEAD`

git clone $REPO out
cd out
git checkout $TARGET_BRANCH || git checkout --orphan $TARGET_BRANCH
cd ..

rm -rf out/**/* || exit 0

doCompile

cd out
git config user.name "Travis CI"
git config user.email "$COMMIT_AUTHOR_EMAIL"

if git diff --quiet; then
    echo "No changes to the output on this push; exiting."
    exit 0
fi

git add -A .
git commit -m "Deploy to GitHub Pages: ${SHA}"

echo $pass | gpg --passphrase-fd 0 deploy_key.gpg
chmod 600 deploy_key
eval `ssh-agent -s`
ssh-add deploy_key

git push $SSH_REPO $TARGET_BRANCH