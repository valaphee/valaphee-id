/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.tinsel.id.account

import org.springframework.security.core.AuthenticationException

/**
 * @author Kevin Ludwig
 */
class DuplicateAuthenticationException(message: String) : AuthenticationException(message)
