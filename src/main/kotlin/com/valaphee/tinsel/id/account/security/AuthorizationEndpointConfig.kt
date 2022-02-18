/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.tinsel.id.account.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.annotation.PostConstruct

/**
 * @author Kevin Ludwig
 */
@Controller
@Configuration
class AuthorizationEndpointConfig(
    private val authorizationEndpoint: AuthorizationEndpoint
) {
    @PostConstruct
    fun setUserApprovalPage() {
        authorizationEndpoint.setUserApprovalPage("forward:/")
    }

    @RequestMapping("/oauth/error")
    fun oauthError() = "error.oauth"
}
