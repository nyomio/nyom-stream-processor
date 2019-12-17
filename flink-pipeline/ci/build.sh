#!/usr/bin/env bash

cd "$(dirname "$0")" || exit

cd ..
./gradlew fatJar

cd ci/flink-container/docker
./build.sh --from-release --flink-version 1.9.1 --scala-version 2.12 \
    --job-artifacts ../../../build/libs/nyomage-streamprocessor-all-0.1.jar \
    --image-name streamprocessor_flink-pipeline
