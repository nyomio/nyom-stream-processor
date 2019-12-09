#!/bin/bash
#eval $(minikube docker-env)

cd ../flink
./build.sh

cd ../nyomio-protocol
./gradlew clean build jibDockerBuild

cd ../flink-pipeline
./gradlew clean fatJar

cd ../dummy-companyapi-impl
./gradlew clean build jibDockerBuild

cd ../dummy-devicemanagerapi-impl
./gradlew clean build jibDockerBuild
