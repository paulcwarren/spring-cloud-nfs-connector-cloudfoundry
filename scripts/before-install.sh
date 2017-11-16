#!/usr/bin/env bash

#openssl aes-256-cbc -K $encrypted_2d46c2ddc73e_key -iv $encrypted_2d46c2ddc73e_iv -in codesigning.asc.enc -out codesigning.asc -d
#gpg --fast-import codesigning.asc

if [ ! -z "$GPG_SECRET_KEYS" ]; then echo $GPG_SECRET_KEYS | base64 --decode | gpg --import; fi
if [ ! -z "$GPG_OWNERTRUST" ]; then echo $GPG_OWNERTRUST | base64 --decode | gpg --import-ownertrust; fi

if [ -n "$TRAVIS_TAG" ]; then
    export BUILD_TYPE=release
else
    export BUILD_TYPE=snapshot
fi