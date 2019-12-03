# Description
Positional data processor using Apache Flink

# Prerequisites
* java 8
* gradle
* nyomage-streamprocessor-infra set up and running

# Run in IDE
* Optional, only if you want to override the defaults. Create a config in `resources/config/` named `yourconfig.yaml`
  ```
  kafkaAddress: localhost:32400
  ```
  * for all configuration options see com.inepex.nyomagestreamprocessor.config.Configuration
* run com.inepex.nyomagestreamprocessor.AppKt
  * with JVM args: -Xms300M -Xmx300M
  * with args: -p yourconfig
* you can verify it's working using Kibana at localhost:5601. Nyomages are stored in nyom index. Also
you can see the logs in flink_log-* index.

# Deploy to Flink cluster
* execute deployjob.py
```
python3 deployjob.py localhost:8081 build/libs/nyomage-streamprocessor-all-0.1.jar
```
* localhost:8081 only works, when port forwarding is enabled.

* verify Running Jobs at Flink Web Dashboard: http://localhost:8081/

# Execute integration tests

# Test with inetracker/javatracker using nyomage-nyomio_protocol

# Test it by uploading test data
* run com.inepex.nyomagestreamprocessor.BasicDeviceManagerAppKt
* run com.inepex.nyomagestreamprocessor.CompanyApiTestImplAppKt
* run com.inepex.nyomagestreamprocessor.DummyIncomingNyomProducerAppKt
* you can verify it's working using Kibana at localhost:5601. Nyomages are stored in nyom index. Also
you can see the logs in flink_log-* index.
