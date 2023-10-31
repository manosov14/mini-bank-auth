package com.example.minibank.controllers

import com.example.minibank.dtos.UserDTO
import com.example.minibank.dtos.exeptions.Message
import com.example.minibank.models.User
import com.example.minibank.services.FTService
import com.example.minibank.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1")
@ConditionalOnProperty(prefix = "feature.toggles", name = ["register"], havingValue = "true")
@Tag(
    name = "Сервис по работе с пользователями",
    description = "Все методы для работы с пользователями",
)

class AuthController(
    private val userService: UserService,
    val ftService: FTService
) {

    private val userCanCreate: String = "userCanCreate"
    private val userCanGet: String = "userCanGet"
    private val userCanDelete: String = "userCanDelete"
    private val userCanLogin: String = "userCanLogin"
    private val userCanUpdate: String = "userCanUpdate"
    private val userCanlogout: String = "logout"

    @PutMapping("/user/{id}")
    @Operation(summary = "Обновление данных пользователя")
    fun updateUser(@PathVariable id: Int, @RequestBody user: UserDTO): Any {
        if (!ftService.isEnabled(userCanUpdate)) {
            println("feature $userCanUpdate is off")
            return ResponseEntity.ok().body(Message("FT $userCanUpdate is off"))
        }
        println("feature $userCanUpdate is on")

        return userService.updateUser(id, user)
    }

    @PostMapping("/user")
    @Operation(summary = "Создание пользователя")
    fun register(@RequestBody body: UserDTO): ResponseEntity<Any> {
        if (!ftService.isEnabled(userCanCreate)) {
            println("feature $userCanCreate is off")
            return ResponseEntity.ok().body(Message("FT $userCanCreate is off"))
        }
        println("feature $userCanCreate is on")

        return try {
            val user = User()
            @Operation(description = "Имя ")
            user.name = body.name

            @Operation(description = "email ")
            user.email = body.email

            @Operation(description = "Пароль пользователя")
            user.password = body.password

            ResponseEntity.ok(this.userService.create(user))
        } catch (e: Exception) {
            ResponseEntity.status(500).body(Message("this user already create"))
        }
    }

    @DeleteMapping("/user/{id}")
    @Operation(summary = "Удаление пользователя")
    fun delete(@CookieValue("jwt") jwt: String?, @PathVariable id: Int): ResponseEntity<Any> {

        if (!ftService.isEnabled(userCanDelete)) {
            println("feature $userCanDelete is off")
            return ResponseEntity.ok().body(Message("FT $userCanDelete is off"))
        }
        println("feature $userCanDelete is on")

        return userService.deleteUser(id)
    }

    @GetMapping("/user")
    @Operation(summary = "Получение данных о пользователе")
    fun getUser(@CookieValue("jwt") jwt: String?): ResponseEntity<Any> {

        if (!ftService.isEnabled(userCanGet)) {
            println("feature $userCanGet is off")
            return ResponseEntity.ok().body(Message("FT $userCanGet is off"))
        }
        println("feature $userCanGet is on")

        try {
            if (jwt == null) {
                return ResponseEntity.status(401).body(Message("unauthorized"))
            }

            println("feature $userCanGet is on")
            val body = Jwts.parser()
                .setSigningKey("secretsecretsecretsecretsecretsecretsecretsecret") // TODO Вынести в конфигурацию
                .parseClaimsJws(jwt).body

            return ResponseEntity.ok(this.userService.getById(body.issuer.toInt()))
        } catch (e: Exception) {
            return ResponseEntity.status(401)
                .body(Message("unauthorized")) // Сообщение при невалидном текене
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя")
    fun login(@RequestBody body: UserDTO, response: HttpServletResponse): ResponseEntity<Any> {

        if (!ftService.isEnabled(userCanLogin)) {
            println("feature $userCanLogin is off")
            return ResponseEntity.ok().body(Message("FT $userCanLogin is off"))
        }
        println("feature $userCanLogin is on")

        val user = this.userService.findByEmail(body.email)
            ?: return ResponseEntity.badRequest().body(Message("user not found"))

        //
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
            .signWith(
                SignatureAlgorithm.HS256,
                "secretsecretsecretsecretsecretsecretsecretsecret",
            ) // TODO Вынести в конфигурацию
            .compact()

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("success"))
    }

    @PostMapping("/logout")
    @Operation(summary = "Деавторизация пользователя")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {

        if (!ftService.isEnabled(userCanlogout)) {
            println("feature $userCanlogout is off")
            return ResponseEntity.ok().body(Message("FT $userCanlogout is off"))
        }
        println("feature $userCanLogin is on")

        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0
        response.addCookie(cookie)
        return ResponseEntity.ok(Message("success"))
    }
}
