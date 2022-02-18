/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id.util

import java.security.KeyPair
import java.security.KeyPairGenerator

/**
 * @author Kevin Ludwig
 */
object KeyUtil {
    fun generateRsaKey(): KeyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(4096) }.generateKeyPair()
}
