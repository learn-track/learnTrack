package ch.learntrack.backend.admin.teacher

import ch.learntrack.backend.common.LearnTrackConflictException
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.daos.UserSchoolDao
import ch.learntrack.backend.persistence.tables.pojos.UserSchool
import ch.learntrack.backend.user.UserDto
import ch.learntrack.backend.user.UserService
import java.util.UUID

public class AdminTeacherService(
    private val userService: UserService,
    private val userSchoolDao: UserSchoolDao,
) {
    public fun getAllTeachersForSchool(schoolId: UUID): List<UserDto> =
        userService.getUsersByRoleAndSchoolId(UserRole.TEACHER, schoolId)

    public fun searchTeacherByEmail(schoolId: UUID, email: String): List<TeacherDto> {
        if (email.length < 3) {
            return emptyList()
        }

        return userService.searchForUserByEmail(email, UserRole.TEACHER)
            .map { user ->
                TeacherDto(
                    id = user.id,
                    email = user.eMail,
                    isAssignedToSchool = userService.getUsersByRoleAndSchoolId(UserRole.TEACHER, schoolId)
                        .any { it.id == user.id },
                )
            }
    }

    public fun assignTeacherToSchool(schoolId: UUID, teacherId: UUID) {
        if (userService.findById(teacherId)?.userRole != UserRole.TEACHER) {
            throw LearnTrackConflictException(
                "User with id $teacherId is not a teacher",
            )
        }

        if (userService.getUsersByRoleAndSchoolId(UserRole.TEACHER, schoolId)
            .any { it.id == teacherId }) {
            throw LearnTrackConflictException(
                "Teacher with id $teacherId " +
                        "is already assigned to school $schoolId",
            )
        }

        userSchoolDao.insert(
            UserSchool(
                userId = teacherId,
                schoolId = schoolId,
            ))
    }
}
