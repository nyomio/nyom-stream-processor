#!/usr/bin/env bash
cd "$(dirname "$0")" || exit

echo not implemented
exit 1

cd ../nyomio-protocol/
gradle clean build jib
