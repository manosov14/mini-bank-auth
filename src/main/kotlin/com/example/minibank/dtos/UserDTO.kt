package com.example.minibank.dtos

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Информация о пользователе")
class UserDTO {
    @Schema(description = "Имя")
    val name: String = ""

    @Schema(description = "Email")
    val email: String = ""

    @Schema(description = "Пароль")
    val password: String = ""
}