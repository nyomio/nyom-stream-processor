#!/usr/bin/env bash
cd "$(dirname "$0")" || exit

# escape sequence to change font style to italic (i) and normal (n)
i=$(tput bold)
n=$(tput sgr0)

currentK8sContext=$(kubectl config current-context)

case "$currentK8sContext" in
  minikube)
    eval $(minikube docker-env)
    ;;
esac

printf "${i}# Building flink\n"
cd ../flink-pipeline/ci
./build.sh
cd ../

printf "${i}\n\n# Building nyomio-protocol\n"
cd ../nyomio-protocol
./gradlew clean build jibDockerBuild

printf "${i}\n\n# Building flink-pipeline\n"
cd ../flink-pipeline
./gradlew clean fatJar

printf "${i}\n\n# Building dummy-companyapi-impl\n"
cd ../dummy-companyapi-impl
./gradlew clean build jibDockerBuild

printf "${i}\n\n# Building dummy-devicemanagerapi-impl\n"
cd ../dummy-devicemanagerapi-impl
./gradlew clean build jibDockerBuild
