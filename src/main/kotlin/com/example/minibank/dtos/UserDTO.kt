package com.example.minibank.dtos

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Информация о пользователе")
class UserDTO {
    @Schema(description = "Имя")
    var name: String = ""

    @Schema(description = "Email")
    var email: String = ""

    @Schema(description = "Пароль")
    var password: String = ""
}
