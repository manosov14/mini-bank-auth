package com.example.minibank.controllers

import com.example.minibank.BaseTest
import com.example.minibank.dtos.UserDTO
import com.example.minibank.dtos.exeptions.Message
import com.example.minibank.models.User
import com.example.minibank.services.FTService
import com.example.minibank.services.UserService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.mock.web.MockHttpServletResponse

class AuthControllerTest : BaseTest() {

    @InjectMocks
    private lateinit var subj: AuthController

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var ftService: FTService

    @Test
    fun register() {
        val body = UserDTO()
        whenever(ftService.isEnabled(any())).thenReturn(true)
        val register = subj.register(body)
        Assertions.assertEquals(200, register.statusCode.value())
    }

    @Test
    fun login() {
        val body = createUserDTO()
        whenever(ftService.isEnabled(any())).thenReturn(true)
        whenever(userService.findByEmail(any())).thenReturn(createUser())
        val login = subj.login(body, MockHttpServletResponse())
        Assertions.assertEquals(200, login.statusCode.value())
    }

    @Test
    fun login_invalidPassword() {
        val body = UserDTO()
        whenever(ftService.isEnabled(any())).thenReturn(true)
        val user = User()
        user.password = "4321"
        whenever(userService.findByEmail(any())).thenReturn(user)
        val login = subj.login(body, MockHttpServletResponse())
        Assertions.assertEquals(400, login.statusCode.value())
    }

    @Test
    fun getUser_badRequest() {
        whenever(ftService.isEnabled(any())).thenReturn(true)
        val responseEntity = subj.getUser("str")
        Assertions.assertEquals(401, responseEntity.statusCode.value())
    }

    @Test
    fun logout() {
        whenever(ftService.isEnabled(any())).thenReturn(true)
        val logout = subj.logout(MockHttpServletResponse())
        Assertions.assertEquals("success", (logout.body as Message).message)
    }
}
