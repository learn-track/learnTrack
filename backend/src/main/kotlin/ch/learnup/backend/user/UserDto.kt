package ch.learnup.backend.user

import ch.learnup.backend.persistence.UserRole
import java.time.LocalDateTime
import java.util.UUID

data class UserDto(
    val id: UUID,
    val email: String,
    val firstname: String,
    val middlename: String?,
    val lastname: String,
    val userRole: UserRole,
    val birthDate: LocalDateTime?,
)

data class LoginDto(
    val email: String,
    val password: String,
)

data class LoginResponseDto(
    val token: String,
)
