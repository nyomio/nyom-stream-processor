package com.inepex.nyomagenyomioprotocol.config

class Configuration {
    var grpcListenPort = 9500
    var grpcUseTls = true
    var grpcCertChainPath = "dev.crt"
    var grpcPrivateKeyPath = "dev.pkcs8"
    var kafkaAddress = "127.0.0.1:32400"
}
