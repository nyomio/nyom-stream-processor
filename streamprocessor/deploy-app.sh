#!/bin/bash

INFRA_DIR="$1"

if [ -z "$INFRA_DIR" ]
then
  echo "usage: deploy-app.sh infra_dir"
  exit
fi

gradle clean fatJar

jobId=`grep --line-buffered JobID $INFRA_DIR/log/deploy-app.log | awk 'NF>1{print $NF}'`
savePointAttr=""
if [ -n "$jobId" ]
then
	echo "Cancelling job: $jobId"
	$INFRA_DIR/flink-1.8.0/bin/flink cancel -s savepoints $jobId
	savePointAttr=" -s savepoints"
else
	echo "Cannot find jobId in $INFRA_DIR/log/deploy-app.log. Skipping cancel."
fi
rm $INFRA_DIR/log/deploy-app.log
$INFRA_DIR/flink-1.8.0/bin/flink run -d build/libs/nyomage-streamprocessor-all-0.1.jar &> $INFRA_DIR/log/deploy-app.log
