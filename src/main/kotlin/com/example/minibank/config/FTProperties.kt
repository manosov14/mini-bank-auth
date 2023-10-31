package com.example.minibank.config

import org.springframework.boot.context.properties.ConfigurationProperties
import kotlin.properties.Delegates

@Suppress("ConfigurationProperties")
@ConfigurationProperties
class FTProperties {

    var features = mapOf<String, FeatureToggle>()
}

class FeatureToggle {
    lateinit var name: String
    var enabled by Delegates.notNull<Boolean>()
}
