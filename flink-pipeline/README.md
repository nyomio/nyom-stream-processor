# Description
Positional data processor using Apache Flink

# Running from IDE
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


# Execute integration tests
TODO

# Test with inetracker/javatracker using nyomage-nyomio_protocol
TODO

