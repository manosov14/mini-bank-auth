package com.example.minibank

import com.example.minibank.dtos.UserDTO
import com.example.minibank.models.User
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
abstract class BaseTest {

    protected fun userDTO(): UserDTO {
        val body = UserDTO()
        body.name = "M"
        body.email = "m@email.example"
        body.password = "1234"
        return body
    }

    protected fun user(): User {
        val user = User()
        user.name = "M"
        user.email = "m@email.example"
        user.password = "1234"
        return user
    }
}
