/*
 * Copyright (c) 2021-2022, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id.account

import com.valaphee.id.Config
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * @author Kevin Ludwig
 */
@Controller
class AccountController(
    private val config: Config
) {
    @GetMapping("/")
    fun index(model: Model) = if (isAuthenticated()) "redirect:${config.landingUrl}" else "redirect:/sign-in"

    @GetMapping("/sign-in")
    fun signIn() = if (isAuthenticated()) "redirect:${config.landingUrl}" else "index"

    @GetMapping("/sign-up")
    fun signUp() = if (isAuthenticated() || !config.signUp) "redirect:${config.landingUrl}" else "index"

    @GetMapping("/change-password")
    fun changePassword(@RequestParam("token") token: String) = if (isAuthenticated()) "redirect:${config.landingUrl}" else "index"

    @GetMapping("/forgot-password")
    fun forgotPassword() = if (isAuthenticated()) "redirect:${config.landingUrl}" else "index"

    private fun isAuthenticated(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        return if (authentication == null || AnonymousAuthenticationToken::class.java.isAssignableFrom(authentication.javaClass)) false else authentication.isAuthenticated
    }
}
