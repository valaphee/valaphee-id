/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id.account.security

import com.nimbusds.jose.jwk.JWKSet
import org.springframework.security.jwt.JwtHelper
import org.springframework.security.jwt.crypto.sign.RsaSigner
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.common.util.JsonParser
import org.springframework.security.oauth2.common.util.JsonParserFactory
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import java.security.KeyPair
import java.security.interfaces.RSAPrivateKey

/**
 * @author Kevin Ludwig
 */
class JwkJwtAccessTokenConverter(
    keyPair: KeyPair,
    jwkSet: JWKSet
) : JwtAccessTokenConverter() {
    private val jsonParser: JsonParser = JsonParserFactory.create()
    private val signer: RsaSigner
    private val kid: String

    override fun encode(accessToken: OAuth2AccessToken, authentication: OAuth2Authentication): String = JwtHelper.encode(
        try {
            jsonParser.formatMap(accessTokenConverter.convertAccessToken(accessToken, authentication))
        } catch (ex: Exception) {
            throw IllegalStateException("Cannot convert access token to JSON", ex)
        }, signer, mapOf("kid" to kid)
    ).encoded

    init {
        super.setKeyPair(keyPair)

        signer = RsaSigner(keyPair.private as RSAPrivateKey)
        kid = jwkSet.keys[0].keyID
    }
}
