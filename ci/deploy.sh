#!/usr/bin/env bash
cd "$(dirname "$0")" || exit

paramValues=""
if [[ -a values_override.yml ]]
then
  paramValues="-f values_override.yml"
fi

#helm uninstall dev
helm dependency update helm
helm upgrade --install nyom-stream-processor helm ${paramValues}
