/*
 * Copyright (c) 2021-2022, GrieferGames, Valaphee.
 * All rights reserved.
 */

package com.valaphee.tinsel.id

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author Kevin Ludwig
 */
@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.apply {
            addResourceHandler("/**/*")
                .addResourceLocations("file:public_html/", "classpath:/static/")
                .setCachePeriod(365 * 24 * 60 * 60)
                .resourceChain(true)
        }
    }
}
