package ch.learnup.backend.user

import ch.learnup.backend.common.LearnupAuthorizationException
import ch.learnup.backend.jwt.TokenService
import ch.learnup.backend.security.PasswordService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
public class UserRessource(
    private val userService: UserService,
    private val passwordService: PasswordService,
    private val tokenService: TokenService,
) {
    @PostMapping("/login")
    public fun login(@RequestBody loginDto: LoginDto): LoginResponseDto {
        val user = userService.findUserByEmail(loginDto.email)
            ?: throw LearnupAuthorizationException("Invalid login credentials")

        if (!passwordService.isPasswordMatchingHash(loginDto.password, user.password)) {
            throw LearnupAuthorizationException("Invalid login credentials")
        }

        return LoginResponseDto(
            token = tokenService.createJwtToken(user.id),
        )
    }

    @GetMapping("/test")
    public fun test(): String = "test string from backend"
}
