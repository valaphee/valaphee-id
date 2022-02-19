/*
 * Copyright (c) 2021-2022, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id.util

import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import java.security.KeyPair
import java.security.interfaces.RSAPublicKey

/**
 * @author Kevin Ludwig
 */
object JwkSetUtil {
    fun generateRsaKey(keyPair: KeyPair): RSAKey = RSAKey.Builder(keyPair.public as RSAPublicKey)/*.privateKey(keyPair.private)*/.keyUse(KeyUse.SIGNATURE).keyIDFromThumbprint().build()
}
