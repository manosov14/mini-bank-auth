package com.example.minibank.dtos

import io.swagger.v3.oas.annotations.media.Schema

class LoginDTO {
    @Schema(description = "Email")
    val email: String = ""

    @Schema(description = "Пароль")
    val password: String = ""
}
