package com.example.minibank.services

import com.example.minibank.BaseTest
import com.example.minibank.repositories.UserRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock

class UserServiceTest : BaseTest() {

    @InjectMocks
    private lateinit var subj: UserService

    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun deleteUser() {
        doNothing().whenever(userRepository).deleteById(1)
        val deleteUser = subj.deleteUser(1)
        Assertions.assertEquals(200, deleteUser.statusCode.value())
    }

    @Test
    fun findByEmail() {
        val findByEmail = subj.findByEmail("m@email.example")
        Assertions.assertNull(findByEmail)
    }

    @Test
    fun getById() {
        whenever(userRepository.getById(any())).thenReturn(user())
        val byId = subj.getById(1)
        Assertions.assertNotNull(byId)
    }
}
