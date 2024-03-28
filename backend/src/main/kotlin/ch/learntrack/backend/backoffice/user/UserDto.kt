package ch.learntrack.backend.backoffice.user

import java.util.UUID

public data class UserDto(
    val id: UUID,
    val firstname: String,
    val lastname: String,
)
