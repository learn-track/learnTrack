 package ch.learntrack.backend.backoffice.user

import java.time.LocalDateTime
import java.util.UUID

public data class UserDto(
    val id: UUID,
    val firstname: String,
    val lastname: String,
    val email: String,
    val birthdate: LocalDateTime?,
    val created: LocalDateTime?,
    val updated: LocalDateTime?,
)

public data class CreateUserDto(
    val firstname: String,
    val middlename: String?,
    val lastname: String,
    val email: String,
    val password: String,
)
