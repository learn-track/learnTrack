package ch.learntrack.backend.admin.subject

import ch.learntrack.backend.user.UserDto
import java.util.UUID

public data class SubjectDetailsDto(
    val id: UUID,
    val name: String,
    val teacher: UserDto?,
)
