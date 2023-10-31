package com.example.minibank.services

import com.example.minibank.config.FeatureToggle


interface FTService {
    fun isEnabled(ftName: String): Boolean

    fun getAll(): Map<String, FeatureToggle>
}