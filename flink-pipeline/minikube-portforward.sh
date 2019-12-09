#!/bin/bash

trap 'kill $(jobs -pr)' SIGINT SIGTERM EXIT

getPodName()
{
	local selector=$1
	name=$(kubectl get pod -l $selector -o jsonpath="{.items[0].metadata.name}")
	echo "$name"
}

kubectl cp $(getPodName app=flink,component=jobmanager):/opt/keystore/keystore_test.jks keystore.jks

kubectl port-forward $(getPodName app=flink,component=jobmanager) 8081 &
kubectl port-forward $(getPodName kibana.k8s.elastic.co/name=kibana) 5601 &
kubectl port-forward $(getPodName elasticsearch.k8s.elastic.co/cluster-name=eck-test) 9200 &
kubectl port-forward $(getPodName k8s-app=logstash) 4560 &
kubectl port-forward $(getPodName k8s-app=nyomage-nyomioprotocol) 9500 &
kubectl port-forward $(getPodName k8s-app=nyomage) 8082 8083 &
kubectl port-forward service/outside-0 32400 &
kubectl port-forward service/outside-1 32401 &

wait
