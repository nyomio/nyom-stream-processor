mkdir -p libs
rm -f libs/*
cd libs
wget https://repo1.maven.org/maven2/ch/qos/logback/logback-core/1.2.3/logback-core-1.2.3.jar
wget https://repo1.maven.org/maven2/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar
wget https://repo1.maven.org/maven2/net/logstash/logback/logstash-logback-encoder/6.1/logstash-logback-encoder-6.1.jar
wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.9.9/jackson-annotations-2.9.9.jar
wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.9.9/jackson-core-2.9.9.jar
wget https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.9.9/jackson-databind-2.9.9.jar
cd ..
docker build -t flink-logback:latest .
#docker tag flink-logback:latest registry.k8s.inepex.net/flink-logback:latest
#docker push registry.k8s.inepex.net/flink-logback:latest
rm -f libs/*
