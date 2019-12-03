#!/bin/bash
# you have to update version manually

# add to /etc/docker/daemon.json and restart docker (sudo systemctl restart docker)
#{
#	"insecure-registries" : ["registry.k8s.inepex.net"]
#}

name="nyomage-streamprocessor"
version="0.4"

./gradlew fatJar
docker build -t $name:$version .
docker tag $name:$version registry.k8s.inepex.net/$name:$version
docker push registry.k8s.inepex.net/$name:$version
