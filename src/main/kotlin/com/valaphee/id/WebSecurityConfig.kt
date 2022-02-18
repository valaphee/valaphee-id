/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id

import com.valaphee.id.util.KeyUtil
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

/**
 * @author Kevin Ludwig
 */
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Bean
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    override fun configure(http: HttpSecurity) {
        http
            /*.headers { it.contentSecurityPolicy("base-uri 'self'; default-src 'self' 'unsafe-inline'; object-src 'none'") }*/
            .csrf { it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) }
            .logout {
                it.logoutRequestMatcher(AntPathRequestMatcher("/sign-out"))
                it.logoutSuccessHandler { request, response, _ -> response.sendRedirect(request.getParameter("redirect_uri") ?: "/sign-in?ref=sign-out") }
            }
            .formLogin { it.loginPage("/sign-in") }
    }

    @Bean
    fun keyPair() = KeyUtil.generateRsaKey()

    @Bean
    fun passwordEncoder(): PasswordEncoder = DelegatingPasswordEncoder(
        "pbkdf2", mapOf(
            "pbkdf2" to Pbkdf2PasswordEncoder().apply { setAlgorithm(Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512) },
            "scrypt" to SCryptPasswordEncoder()
        )
    )
}
