package ch.learnup.backend.whoami

import ch.learnup.backend.grade.GradeDto
import ch.learnup.backend.school.SchoolDto
import ch.learnup.backend.user.UserDto

data class WhoamiDto(
    val user: UserDto,
    val grade: List<GradeDto>,
    val school: List<SchoolDto>,
)
