/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id

import com.valaphee.id.account.DuplicateAuthenticationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * @author Kevin Ludwig
 */
@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val violations = mutableMapOf<String, String>()
        ex.bindingResult.fieldErrors.forEach { violation -> violations.compute(violation.field) { _, value -> value?.let { it + ",${violation.defaultMessage}" } ?: violation.defaultMessage } }
        return ResponseEntity(Error("Validation Failed", violations.values.joinToString(",")), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleDuplicateAuthenticationException(ex: DuplicateAuthenticationException, request: WebRequest) = ResponseEntity(Error(ex.message!!, ex.message), HttpStatus.BAD_REQUEST)
}
