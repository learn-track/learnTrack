package ch.learntrack.backend.admin.grade

import ch.learntrack.backend.admin.subject.SubjectDetailsDto
import ch.learntrack.backend.grade.GradeDto
import ch.learntrack.backend.user.UserDto
import java.util.UUID

public data class GradeDetailsDto(
    val students: List<UserDto>,
    val subjectDetailsDto: List<SubjectDetailsDto>,
)

public data class GradeInfoDto(
    val grades: GradeDto,
    val students: List<UserDto>,
    val teachers: List<UserDto>,
)

public data class CreateGradeDto(
    val name: String,
    val schoolId: UUID,
)
