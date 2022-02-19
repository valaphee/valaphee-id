/*
 * Copyright (c) 2021-2022, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id.account.security

import com.nimbusds.jose.jwk.JWKSet
import com.valaphee.id.util.JwkSetUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import java.security.KeyPair
import javax.sql.DataSource

/**
 * @author Kevin Ludwig
 */
@Configuration
@Import(AuthorizationServerEndpointsConfiguration::class)
class AuthorizationServerConfig(
    private val dataSource: DataSource,
    private val authenticationManager: AuthenticationManager,
    private val keyPair: KeyPair
) : AuthorizationServerConfigurerAdapter() {
    private val jwkSet = JWKSet(JwkSetUtil.generateRsaKey(keyPair))

    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security.checkTokenAccess("isAuthenticated()")
    }

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients
            .jdbc(dataSource)
            .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder())
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints
            .accessTokenConverter(accessTokenConverter())
            .tokenStore(tokenStore())
            .authenticationManager(authenticationManager)
            .authorizationCodeServices(JdbcAuthorizationCodeServices(dataSource))
    }

    @Bean
    fun accessTokenConverter() = JwkJwtAccessTokenConverter(keyPair, jwkSet)

    @Bean
    fun tokenStore() = JwtTokenStore(accessTokenConverter()).apply { setApprovalStore(JdbcApprovalStore(dataSource)) }

    @Bean
    fun jwkSet() = jwkSet
}
