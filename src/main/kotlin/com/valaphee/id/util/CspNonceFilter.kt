/*
 * Copyright (c) 2021-2022, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id.util

import org.springframework.web.filter.GenericFilterBean
import java.security.SecureRandom
import java.util.Base64
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponseWrapper

/**
 * @author Kevin Ludwig
 */
class CspNonceFilter : GenericFilterBean() {
    private val random = SecureRandom()

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val nonceData = ByteArray(nonceSize)
        random.nextBytes(nonceData)

        val nonce = base64Encoder.encodeToString(nonceData)
        request.setAttribute(cspNonceAttribute, nonce)

        chain.doFilter(request, ResponseWrapper(response as HttpServletResponse, nonce))
    }

    class ResponseWrapper(
        response: HttpServletResponse,
        private val nonce: String
    ) : HttpServletResponseWrapper(response) {
        override fun setHeader(name: String, value: String) {
            super.setHeader(name, if (name == "Content-Security-Policy" && value.isNotBlank()) value.replace("{nonce}", nonce) else value)
        }

        override fun addHeader(name: String, value: String) {
            super.addHeader(name, if (name == "Content-Security-Policy" && value.isNotBlank()) value.replace("{nonce}", nonce) else value)
        }
    }

    companion object {
        private const val nonceSize = 16
        private const val cspNonceAttribute = "cspNonce"
        private val base64Encoder = Base64.getEncoder()
    }
}
