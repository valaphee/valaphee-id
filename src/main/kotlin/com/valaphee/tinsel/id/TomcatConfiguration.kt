/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.tinsel.id

import org.apache.catalina.connector.Connector
import org.apache.coyote.ajp.AbstractAjpProtocol
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Kevin Ludwig
 */
@Configuration
class TomcatConfiguration {
    @Value("\${server.ajp.enabled:false}")
    var ajpEnabled = false

    @Value("\${server.ajp.port:9090}")
    var ajpPort = 9090

    @Bean
    fun servletContainer() = TomcatServletWebServerFactory().apply {
        if (ajpEnabled) addAdditionalTomcatConnectors(Connector("AJP/1.3").apply {
            port = ajpPort
            (protocolHandler as AbstractAjpProtocol<*>).secretRequired = false
        })
    }
}
