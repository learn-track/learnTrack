package ch.learntrack.backend.admin.student

import ch.learntrack.backend.grade.GradeDto
import ch.learntrack.backend.user.UserDto

public data class StudentDetailsDto(
    val user: UserDto,
    val grade: GradeDto,
)
