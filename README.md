# nyom-stream-processor
Data processing modules, inluding Apache Flink pipeline, device prototocol.

## Add an entry to /etc/hosts
```
10.96.218.228 kibana-dev.nyomio.local elasticsearch-dev.nyomio.local flink-dev.nyomio.local nyomio-protocol-dev.nyomio.local traefik.nyomio.local dummy-companyapi-impl-dev.nyomio.local dummy-devicemangerapi-impl-dev.nyomio.local app.nyomio.local sso.nyomio.local 
```

## Resolve missing gradle properties for inepex maven repository access
* Request password, and replace `secretpassword`. 
* Create a gradle.properties in GRADLE_USER_HOME. On linux the default is ~/.gradle.
```
inepexExternalMavenUser=external
inepexExternalMavenPassword=secretpassword
inepexInternalMavenUser=deployment
inepexInternalMavenPassword=secretpassword
inepexPublishMavenUser=deployment
inepexPublishMavenPassword=secretpassword
```
