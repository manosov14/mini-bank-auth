package com.example.minibank.repositories

import com.example.minibank.models.User
import org.springframework.data.jpa.repository.JpaRepository
interface UserRepository : JpaRepository<User, Int> {

}