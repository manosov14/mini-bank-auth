package com.example.minibank.services.impl

import com.example.minibank.config.FTProperties
import com.example.minibank.config.FeatureToggle
import com.example.minibank.services.FTService

import org.springframework.stereotype.Service

@Service
class FTServiceImpl(val ftProps: FTProperties) : FTService {

    override fun isEnabled(ftName: String): Boolean {
        return ftProps.features.get(ftName)?.enabled ?: false
    }

    override fun getAll(): Map<String, FeatureToggle> {
        return ftProps.features
    }
}