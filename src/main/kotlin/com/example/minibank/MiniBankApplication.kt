package com.example.minibank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class MiniBankApplication

fun main(args: Array<String>) {
    runApplication<MiniBankApplication>(*args)
}
