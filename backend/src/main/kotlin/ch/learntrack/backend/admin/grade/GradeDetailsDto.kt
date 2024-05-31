package ch.learntrack.backend.admin.grade

import ch.learntrack.backend.grade.GradeDto
import ch.learntrack.backend.user.UserDto

public data class GradeDetailsDto(
    val grades: List<GradeDto>,
    val users: List<UserDto>,
)
