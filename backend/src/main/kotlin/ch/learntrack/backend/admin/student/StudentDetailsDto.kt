package ch.learntrack.backend.admin.student

import ch.learntrack.backend.grade.GradeDto
import ch.learntrack.backend.user.UserDto
import java.time.LocalDate
import java.util.UUID

public data class StudentDetailsDto(
    val user: UserDto,
    val grade: GradeDto,
)

public data class CreateStudentDto(
    val firstname: String,
    val middlename: String?,
    val lastname: String,
    val email: String,
    val birthDate: LocalDate,
    val password: String,
    val schoolId: UUID,
    val gradeId: UUID,
)
