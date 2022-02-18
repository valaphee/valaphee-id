/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.tinsel.id

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * @author Kevin Ludwig
 */
@ConstructorBinding
@ConfigurationProperties("tinsel.id")
class Config(
    var brandName: String = "Unknown",
    var brandLogo: String? = null,
    var baseUrl: String = "http://localhost:8080/",
    var noReplyEmail: String = "no-reply@localhost",
    var signUp: Boolean = true,
    var changePasswordExpiresIn: Long = 30 * 60 * 1000L,
    var landingUrl: String = "http://localhost:8080/"
)
