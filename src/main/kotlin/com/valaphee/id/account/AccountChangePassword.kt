/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id.account

import org.springframework.data.jpa.repository.JpaRepository
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

/**
 * @author Kevin Ludwig
 */
@Entity
data class AccountChangePassword(
    @Column(unique = true, nullable = false) var token: String,
    @Column(nullable = false) var expiresAt: Timestamp,
    @OneToOne @JoinColumn(nullable = false) var account: Account,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int? = null
)

/**
 * @author Kevin Ludwig
 */
interface AccountChangePasswordRepository : JpaRepository<AccountChangePassword, Int> {
    fun findByToken(code: String): AccountChangePassword?

    fun existsByAccount(account: Account): Boolean

    fun findAllByExpiresAtBefore(currentTime: Timestamp): List<AccountChangePassword>
}
