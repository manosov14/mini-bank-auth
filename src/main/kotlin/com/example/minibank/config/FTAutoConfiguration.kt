package com.example.minibank.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("feature-toggles")
@Configuration
@EnableConfigurationProperties(FTProperties::class)
class FTAutoConfiguration
