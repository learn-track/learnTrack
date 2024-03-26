package ch.learntrack.backend.whoami

import ch.learntrack.backend.grade.GradeDto
import ch.learntrack.backend.school.SchoolDto
import ch.learntrack.backend.user.UserDto

public data class WhoamiDto(
    val user: UserDto,
    val grades: List<GradeDto>,
    val schools: List<SchoolDto>,
)
