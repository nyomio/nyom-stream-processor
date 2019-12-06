#!/bin/bash
#cd ../nyomio-protocol
#helm install helm --set image.pullPolicy=Never
helm uninstall dev
helm dependency update helm
#helm install helm --set global.image.pullPolicy=Never
#helm install helm --debug --dry-run --set global.imagePullPolicy=Never
helm install dev helm
