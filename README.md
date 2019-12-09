# nyom-stream-processor
Data processing modules, inluding Apache Flink pipeline, device prototocol.

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
