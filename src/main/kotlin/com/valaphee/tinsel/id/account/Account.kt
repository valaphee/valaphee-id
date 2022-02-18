/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.tinsel.id.account

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * @author Kevin Ludwig
 */
@Entity
data class Account(
    @Column(unique = true, nullable = false)
    var username: String,

    @Column(unique = true, nullable = false)
    var email: String,

    @Column
    var password: String? = null,

    @Column
    var roles: String? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
)

/**
 * @author Kevin Ludwig
 */
interface AccountRepository : JpaRepository<Account, Int> {
    fun findByUsername(username: String): Account?

    fun findByEmail(email: String): Account?
}
