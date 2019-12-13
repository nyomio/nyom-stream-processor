#!/usr/bin/env bash
cd "$(dirname "$0")" || exit

#helm uninstall dev
helm dependency update helm
helm install dev helm
