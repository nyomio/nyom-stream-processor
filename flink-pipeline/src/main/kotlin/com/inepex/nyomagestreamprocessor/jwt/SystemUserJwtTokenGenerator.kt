package com.inepex.nyomagestreamprocessor.jwt

import io.micronaut.security.authentication.UserDetails
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator

class SystemUserJwtTokenGenerator constructor(private val jwtTokenGenerator: JwtTokenGenerator) {

    fun generate(): String {
        return jwtTokenGenerator.generateToken(
                UserDetails("system", listOf("admin")), 5).get()
    }
}
