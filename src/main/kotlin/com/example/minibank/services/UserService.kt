package com.example.minibank.services

import com.example.minibank.dtos.UserDTO
import com.example.minibank.dtos.exeptions.Message
import com.example.minibank.models.User
import com.example.minibank.repositories.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun create(user: User): User {
        return this.userRepository.save(user)
    }

    fun deleteUser(id: Int): ResponseEntity<Any> {
        try {
            this.userRepository.findByIdOrNull(id)
            return ResponseEntity.ok(this.userRepository.deleteById(id))
        } catch (e: Exception) {
            return ResponseEntity.ok(Message("User with id $id not found"))
        }
    }

    @Transactional
    fun updateUser(id: Int, user: UserDTO) {

        val existingUser = userRepository.findByIdOrNull(id)
            ?: throw RuntimeException("User with id $id not found")

        existingUser.name = user.name
        existingUser.email = user.email
        existingUser.password = user.password
        userRepository.save(existingUser)
    }

    fun findByEmail(email: String): User? {
        return this.userRepository.findByEmail(email)
    }

    fun getById(id: Int): User {
        return this.userRepository.getById(id) // TODO Заменить на актуальный метод
    }
}
