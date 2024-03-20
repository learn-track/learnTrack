package ch.learnup.backend.whoami

import ch.learnup.backend.grade.GradeDto
import ch.learnup.backend.school.SchoolDto
import ch.learnup.backend.user.UserDto

data class WhoamiDto(
    val user: UserDto,
    val grades: List<GradeDto>,
    val schools: List<SchoolDto>,
)
