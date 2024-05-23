package ch.learntrack.backend.user

import ch.learntrack.backend.common.LearnTrackAuthorizationException
import ch.learntrack.backend.security.PasswordService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
public class UserResource(
    private val userService: UserService,
    private val passwordService: PasswordService,
    private val tokenService: ch.learntrack.backend.jwt.TokenService,
) {
    @PostMapping("/login")
    public fun login(@RequestBody loginDto: LoginDto): LoginResponseDto {
        val user = userService.findUserByEmail(loginDto.email.lowercase())
            ?: throw LearnTrackAuthorizationException("Invalid login credentials")

        if (!passwordService.isPasswordMatchingHash(loginDto.password, user.password)) {
            throw LearnTrackAuthorizationException("Invalid login credentials")
        }

        return LoginResponseDto(
            token = tokenService.createJwtToken(user.id),
        )
    }

    @GetMapping("/register/check-email-free")
    public fun isEmailFree(@RequestParam email: String): Boolean = userService.findUserByEmail(email) == null

    @PostMapping("/register")
    public fun register(@RequestBody createUserDto: CreateUserDto): LoginResponseDto {
        val user = userService.createUser(createUserDto)

        return LoginResponseDto(
            token = tokenService.createJwtToken(user.id),
        )
    }

    @GetMapping("/test")
    public fun test(): String = "test string from backend"
}
