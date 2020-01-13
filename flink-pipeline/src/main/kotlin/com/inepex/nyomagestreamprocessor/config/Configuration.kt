package com.inepex.nyomagestreamprocessor.config

class Configuration {
    var elasticHost = "elasticsearch-default.nyomio.local"
    var elasticPort = 443
    var elasticUser = "elastic"
    var elasticPassword = "f2Tt8YXBUJRipRCk"
    var keystorePath = "java_keystore.jks"
    var keystorePass = "developer"
    var kafkaAddress = "traefik.nyomio.local:32400"
    var deviceManagerUrl = "https://dummy-devicemangerapi-impl-default.nyomio.local"
    var testCompanyApiUrl = "https://dummy-companyapi-impl-default.nyomio.local"
}
