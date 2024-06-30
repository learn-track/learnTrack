package ch.learntrack.backend.user

import ch.learntrack.backend.persistence.UserRole
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

public data class UserDto(
    val id: UUID,
    val email: String,
    val firstname: String,
    val middlename: String?,
    val lastname: String,
    val userRole: UserRole,
    val birthDate: LocalDateTime?,
)

public data class LoginDto(
    val email: String,
    val password: String,
)

public data class LoginResponseDto(
    val token: String,
)

public data class CreateUserDto(
    val firstname: String,
    val middlename: String?,
    val lastname: String,
    val email: String,
    val birthDate: LocalDate?,
    val password: String,
)
