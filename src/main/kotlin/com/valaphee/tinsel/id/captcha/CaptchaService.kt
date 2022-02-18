/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.tinsel.id.captcha

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.stereotype.Service

/**
 * @author Kevin Ludwig
 */
@Service
class CaptchaService {
    fun check(response: String) {

    }
}

/**
 * @author Kevin Ludwig
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SiteVerifyResponse(
    @JsonProperty("success") val success: Boolean,
    @JsonProperty("challenge_ts") val challengeTs: String?,
    @JsonProperty("hostname") val hostname: String?,
    @JsonProperty("error-codes") val errorCodes: List<ErrorCode>
) {
    enum class ErrorCode {
        @JsonProperty("missing-input-secret") MissingSecret,
        @JsonProperty("invalid-input-secret") InvalidSecret,
        @JsonProperty("missing-input-response") MissingResponse,
        @JsonProperty("invalid-input-response") InvalidResponse
    }
}
