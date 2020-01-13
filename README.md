# nyom-stream-processor
Data processing modules, inluding Apache Flink pipeline, device protocol.

## Prerequisites
* Java 8
* Gradle
* Maven
* Kubernetes cluster. Tested with minikube
* Helm v3
* nyomio-framework/certs created for your domain - https://github.com/nyomio/nyomio-framework/tree/master/certs
* nyomio-framework/traefik v2 installed - https://github.com/nyomio/nyomio-framework

## Getting started - local development
* register nyomio.local domains in /etc/hosts
  * get ip of minikube: `$ minikube ip`
  * add an entry to /etc/hosts, replace ip with minikube ip
    ```
    10.102.46.80 kibana-dev.nyomio.local elasticsearch-dev.nyomio.local flink-dev.nyomio.local nyomio-protocol-dev.nyomio.local traefik.nyomio.local dummy-companyapi-impl-nyom-stream-processor.nyomio.local dummy-devicemangerapi-impl-nyom-stream-processor.nyomio.local kafka-manager-dev.nyomio.local app.nyomio.local sso.nyomio.local
    ```
  * verify that traefik dashboard can be accessed in the browser: `https://traefik.nyomio.local/` 
  (important: you have to write https)
* set credential for inepex maven repository
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
* build the project
```
$ cd ci
$ ./build.sh
```
  * verify that you're using docker of your kubernetes node, and that docker images were created: `docker images`
    * streamprocessor_flink-pipeline
    * streamprocessor_nyomio-protocol
    * streamprocessor_dummy-companyapi-impl
    * streamprocessor_dummy-devicemanagerapi-impl
* deploy project using helm
```
$ cd ci
$ ./deploy.sh
```
* verify the following:
  * access flink admin ui in browser. There should be 1 running job. `https://flink-dev.nyomio.local/`
  * access kibana. User: elastic, pass: f2Tt8YXBUJRipRCk `https://kibana-dev.nyomio.local/`
    * you can't create index patterns until indexes are empty, but there should be 3 indexes:
    `schema_version`, `trip`, `nyom`
  * connect to nyomio-protocol using a nyomio compatible tracker client. Endpoint: `nyomio-protocol-dev.nyomio.local`, port: `443`
    * Important! Use the same cert for running the client which you use for your domain




