#!/bin/bash

trap 'kill $(jobs -pr)' SIGINT SIGTERM EXIT

getPodName()
{
	local selector=$1
	name=$(kubectl get pod -l $selector -o jsonpath="{.items[0].metadata.name}")
	echo "$name"
}

#kubectl cp $(getPodName app=flink,component=jobmanager):/root/cert/java_keystore.jks keystore.jks

kubectl port-forward $(getPodName app=logstash) 4560 &
#kubectl port-forward service/outside-0 32400 &
#kubectl port-forward service/outside-1 32401 &

wait
