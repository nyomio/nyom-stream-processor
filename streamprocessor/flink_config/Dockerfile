FROM flink:latest
MAINTAINER daniel.kovacs@inepex.com
RUN rm /opt/flink/lib/log4j*.jar && \
    rm /opt/flink/lib/slf4j-log4j*.jar
COPY libs/* /opt/flink/lib/
COPY logback.xml /opt/flink/conf/logback-console.xml

