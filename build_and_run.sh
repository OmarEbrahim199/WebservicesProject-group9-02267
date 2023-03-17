#!/bin/bash
set -e
./build.sh

# Give the Web server a chance to finish start up
pushd End-to-end-queue
./deploy.sh
sleep 5
./test.sh
popd
# Cleanup the build images
docker image prune -f
