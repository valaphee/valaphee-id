/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.tinsel.id.account.security

import com.nimbusds.jose.jwk.JWKSet
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Kevin Ludwig
 */
@RestController
class JwkSetController(
    private val jwkSet: JWKSet
) {
    @RequestMapping("/.well-known/jwks.json")
    fun getKey(): Map<String, Any> = jwkSet.toJSONObject()
}
