package com.example.minibank.models

//import jakarta.persistence.Column
//import jakarta.persistence.Entity
//import jakarta.persistence.GeneratedValue
//import jakarta.persistence.GenerationType
//import jakarta.persistence.Id
//import jakarta.persistence.Table

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.persistence.*

@Entity
@Table(name = "user_table")

class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0

    @Column
    var name: String = ""

    @Column(unique = true)
    var email: String = ""

    @Column
    var password: String = ""
        @JsonIgnore
        get() = field
        set(value) {
            val passwordEncoder = BCryptPasswordEncoder()
            field = passwordEncoder.encode(value)
        }

    fun comparePassword(password: String): Boolean {
        return BCryptPasswordEncoder().matches(password, this.password )

    }
}
