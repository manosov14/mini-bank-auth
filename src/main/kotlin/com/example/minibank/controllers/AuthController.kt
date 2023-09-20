package com.example.minibank.controllers

import com.example.minibank.dtos.LoginDTO
import com.example.minibank.dtos.RegisterDTO
import com.example.minibank.dtos.exepsions.Message
import com.example.minibank.models.User
import com.example.minibank.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("api")
@ConditionalOnProperty(prefix = "feature.toggles", name = ["register"], havingValue="true")

class AuthController(private val userService: UserService) {

    @PostMapping("register")
    fun register(@RequestBody body: RegisterDTO): ResponseEntity<Any> {

        return try {
            val user = User()
            user.name = body.name
            user.email = body.email
            user.password = body.password

            ResponseEntity.ok(this.userService.save(user))
        } catch (e: Exception) {
            ResponseEntity.status(500).body(Message("this user already create"))
        }
    }

    @PostMapping("login")
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
        val user = this.userService.findByEmail(body.email)
            ?: return ResponseEntity.badRequest().body(Message("user not found"))

        if (!user.comparePassword(body.password)) {
            return ResponseEntity.badRequest().body(Message("invalid password "))
        }

        val issuer = user.id.toString()

        fun sha256(input: String): String {
            val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
            return String.format("%064x", BigInteger(1, bytes))
        }

        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // TODO Поработать над сроком жизни токена, сейчас проверка временно отключена
//            .signWith("secret", SignatureAlgorithm.ES512)  // Реализация требует объекта Key, TODO Поработать над использованием целевого модификатора, сейчас использует isDeprecated
            .signWith(SignatureAlgorithm.HS256, "secretsecretsecretsecretsecretsecretsecretsecret") // TODO Вынести в конфигурацию
            .compact()

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("success"))
    }

    @GetMapping("user")
    fun user(@CookieValue("jwt") jwt: String?): ResponseEntity<Any> {
        try {
            if (jwt == null) {
                return ResponseEntity.status(401).body(Message("unautentificated"))
            }

            val body = Jwts.parser()
                .setSigningKey("secretsecretsecretsecretsecretsecretsecretsecret") // TODO Вынести в конфигурацию
                .parseClaimsJws(jwt).body

            return ResponseEntity.ok(this.userService.getById(body.issuer.toInt()))
        } catch (e: Exception) {
            return ResponseEntity.status(401)
                .body(Message("unautentificated"))  // TODO будет вызван когда токен не валиден

        }
    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("success"))

    }

}