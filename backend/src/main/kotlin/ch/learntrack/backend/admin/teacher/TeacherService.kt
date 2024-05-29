package ch.learntrack.backend.admin.teacher

import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.user.UserDto
import ch.learntrack.backend.user.UserService
import java.util.UUID

public class TeacherService(
    private val userService: UserService,
) {
    public fun getAllTeachersForSchool(schoolId: UUID): List<UserDto> =
        userService.getUsersByRoleAndSchoolId(UserRole.TEACHER, schoolId)
}
