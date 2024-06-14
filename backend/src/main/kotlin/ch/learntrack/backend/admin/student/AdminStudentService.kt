package ch.learntrack.backend.admin.student

import ch.learntrack.backend.grade.GradeService
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.user.UserService
import java.util.UUID

public class AdminStudentService(
    private val userService: UserService,
    private val gradeService: GradeService,
) {
    public fun getAllStudentsForSchool(schoolId: UUID): List<StudentDetailsDto> =
        userService.getUsersByRoleAndSchoolId(UserRole.STUDENT, schoolId).map { user ->
            StudentDetailsDto(
                user = user,
                grade = gradeService.getGradesForUserByUserId(user.id).first(),
            )
        }
}
