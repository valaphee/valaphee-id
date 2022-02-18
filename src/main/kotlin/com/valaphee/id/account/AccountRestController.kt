/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id.account

import com.valaphee.id.Config
import com.valaphee.id.util.Password
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.Size

/**
 * @author Kevin Ludwig
 */
@RestController
class AccountRestController(
    private val accountService: AccountService,
    private val config: Config
) {
    @PostMapping("/sign-up")
    fun signUp(@RequestBody @Valid form: SignUpForm) {
        if (config.signUp) accountService.signUp(form.username, form.email)
    }

    @PostMapping("/change-password")
    fun changePassword(@RequestParam("token") token: String, @RequestBody @Valid form: ChangePasswordForm) {
        accountService.changePassword(token, form.password)
    }

    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestBody @Valid form: ForgotPasswordForm) {
        accountService.forgotPassword(form.username)
    }
}

/**
 * @author Kevin Ludwig
 */
data class SignUpForm(
    @get:Size(min = 5, max = 24) val username: String,
    @get:Email val email: String
)

/**
 * @author Kevin Ludwig
 */
data class ChangePasswordForm(
    @get:Password val password: String
)

/**
 * @author Kevin Ludwig
 */
data class ForgotPasswordForm(
    val username: String
)
