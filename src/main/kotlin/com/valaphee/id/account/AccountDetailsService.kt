/*
 * Copyright (c) 2021-2022, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id.account

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * @author Kevin Ludwig
 */
@Service
class AccountDetailsService(
    private val repository: AccountRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = repository.findByUsername(username) ?: repository.findByEmail(username) ?: throw UsernameNotFoundException("User $username not found.")
        user.password ?: throw UsernameNotFoundException("User $username not verified.")
        return User
            .withUsername(user.username)
            .password(user.password)
            .roles(*user.roles?.split(',')?.toTypedArray() ?: emptyArray())
            .build()
    }
}
