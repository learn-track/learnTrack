package ch.learntrack.backend.admin.student

import ch.learntrack.backend.grade.GradeService
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.user.CreateUserDto
import ch.learntrack.backend.user.UserService
import java.util.UUID

public class AdminStudentService(
    private val userService: UserService,
    private val gradeService: GradeService,
) {
    public fun getAllStudentsWithDetailsForSchool(schoolId: UUID): List<StudentDetailsDto> =
        userService.getUsersByRoleAndSchoolId(UserRole.STUDENT, schoolId).map { user ->
            StudentDetailsDto(
                user = user,
                grade = gradeService.getGradesForUserByUserId(user.id).first(),
            )
        }

    public fun createStudent(createStudentDto: CreateStudentDto) {
        val createUserDto = CreateUserDto(
            firstname = createStudentDto.firstname,
            middlename = createStudentDto.middlename,
            lastname = createStudentDto.lastname,
            email = createStudentDto.email,
            birthDate = createStudentDto.birthDate,
            password = createStudentDto.password,
        )

        userService.createUser(createUserDto, UserRole.STUDENT, createStudentDto.schoolId, createStudentDto.gradeId)
    }
}
