#!/usr/bin/env bash
cd "$(dirname "$0")" || exit

cat <<-'EOF'
Important. Set docker environment before running this script.
  - minikube: eval $(minikube docker-env)'
EOF

cd ../flink-pipeline/ci
./build.sh
cd ../

cd ../nyomio-protocol
./gradlew clean build jibDockerBuild

cd ../flink-pipeline
./gradlew clean fatJar

cd ../dummy-companyapi-impl
./gradlew clean build jibDockerBuild

cd ../dummy-devicemanagerapi-impl
./gradlew clean build jibDockerBuild
