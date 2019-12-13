package com.inepex.nyomagestreamprocessor.elastic

import com.google.inject.Inject
import com.inepex.nyomagestreamprocessor.config.Configuration
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder
import org.apache.http.ssl.SSLContexts
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyStore
import javax.net.ssl.SSLContext


class SetSecureConnectionService
@Inject constructor(private val config: Configuration){

    fun execute(httpAsyncClientBuilder: HttpAsyncClientBuilder) {
        httpAsyncClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE) // to access elastic running on minikube from the IDE
        httpAsyncClientBuilder.setDefaultCredentialsProvider(getCredentials())
        httpAsyncClientBuilder.setSSLContext(getSslContext())
    }

    private fun getCredentials(): CredentialsProvider {
        val credentialsProvider = BasicCredentialsProvider()
        credentialsProvider.setCredentials(AuthScope.ANY,
                UsernamePasswordCredentials(config.elasticUser, config.elasticPassword))
        return credentialsProvider
    }

    private fun getSslContext(): SSLContext {
        val trustStore = KeyStore.getInstance("jks")
        Files.newInputStream(Paths.get(config.keystorePath)).use { `is` ->
            trustStore.load(`is`, config.keystorePass.toCharArray())
        }
        return SSLContexts.custom()
                .loadTrustMaterial(trustStore, null)
                .build()
    }
}
