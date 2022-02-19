/*
 * Copyright (c) 2021-2022, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id.account.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration

/**
 * @author Kevin Ludwig
 */
@Configuration
internal class AuthorizationServerSecurityConfig : AuthorizationServerSecurityConfiguration() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        http
            .requestMatchers { it.mvcMatchers("/.well-known/jwks.json") }
            .authorizeRequests { it.mvcMatchers("/.well-known/jwks.json").permitAll() }
    }
}
