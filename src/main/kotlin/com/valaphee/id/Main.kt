/*
 * Copyright (c) 2021-2022, Valaphee.
 * All rights reserved.
 */

package com.valaphee.id

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * @author Kevin Ludwig
 */
@SpringBootApplication
@EnableConfigurationProperties(Config::class)
@EnableScheduling
class Main

fun main(arguments: Array<String>) {
    runApplication<Main>(*arguments)
}
