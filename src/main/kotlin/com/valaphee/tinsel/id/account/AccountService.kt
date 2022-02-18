/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.tinsel.id.account

import com.valaphee.tinsel.id.Config
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.security.SecureRandom
import java.sql.Timestamp
import java.util.Base64
import java.util.concurrent.Executors

/**
 * @author Kevin Ludwig
 */
@Service
@Transactional
class AccountService(
    private val accountRepository: AccountRepository,
    private val accountChangePasswordRepository: AccountChangePasswordRepository,
    private val mailSender: JavaMailSender,
    private val passwordEncoder: PasswordEncoder,
    private val config: Config,
    private val templateEngine: TemplateEngine
) {
    private val executor = Executors.newWorkStealingPool()

    fun signUp(username: String, email: String) {
        if (accountRepository.findByUsername(username) != null) throw DuplicateAuthenticationException("Username is already taken.")
        if (accountRepository.findByEmail(email) != null) throw DuplicateAuthenticationException("E-Mail has already been taken.")

        changePasswordRequest(accountRepository.save(Account(username, email)))
    }

    fun changePassword(token: String, password: String) {
        val accountChangePassword = accountChangePasswordRepository.findByToken(token) ?: throw DuplicateAuthenticationException("Token $token not found.")
        accountChangePassword.account.password = passwordEncoder.encode(password)
        accountRepository.save(accountChangePassword.account)
        accountChangePasswordRepository.delete(accountChangePassword)
    }

    fun forgotPassword(usernameOrEmail: String) {
        executor.execute {
            accountRepository.findByUsername(usernameOrEmail)?.let {
                if (!accountChangePasswordRepository.existsByAccount(it)) changePasswordRequest(it)
            } ?: accountRepository.findByEmail(usernameOrEmail)?.let {
                if (!accountChangePasswordRepository.existsByAccount(it)) changePasswordRequest(it)
            }
        } // Prevents timing attack
    }

    @Scheduled(fixedRate = 60 * 1_000)
    fun changePasswordExpiry() {
        val expiredAccountChangePassword = accountChangePasswordRepository.findAllByExpiresAtBefore(Timestamp(System.currentTimeMillis()))
        expiredAccountChangePassword.forEach {
            accountChangePasswordRepository.delete(it)
            if (it.account.password == null) accountRepository.delete(it.account)
        }
    }

    fun changePasswordRequest(account: Account) {
        val accountChangePasswordToken = generateEncodedToken()
        accountChangePasswordRepository.save(AccountChangePassword(accountChangePasswordToken, Timestamp(System.currentTimeMillis() + config.changePasswordExpiresIn), account))

        val message = mailSender.createMimeMessage()
        val messageHelper = MimeMessageHelper(message)
        messageHelper.setFrom(config.noReplyEmail)
        messageHelper.setTo(account.email)
        messageHelper.setSubject("${config.brandName} - Change Password")
        messageHelper.setText(templateEngine.process("email/change-password.html", Context().apply {
            setVariable("url", "${config.baseUrl}change-password?token=${accountChangePasswordToken}")
        }), true)
        mailSender.send(message)
    }

    private fun generateEncodedToken(): String {
        val token = ByteArray(8)
        secureRandom.nextBytes(token)
        return base64Encoder.encodeToString(token)
    }

    companion object {
        private val secureRandom = SecureRandom()
        private val base64Encoder = Base64.getUrlEncoder()
    }
}
