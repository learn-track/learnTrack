package ch.learntrack.backend.admin.grade

import ch.learntrack.backend.grade.GradeDto
import ch.learntrack.backend.user.UserDto
import java.util.UUID

public data class GradeDetailsDto(
    val grade: GradeDto,
    val students: List<UserDto>,
    val teachers: List<UserDto>,
)

public data class CreateGradeDto(
    val name: String,
    val schoolId: UUID,
)
