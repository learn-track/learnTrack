package ch.learntrack.backend.admin.grade

import ch.learntrack.backend.admin.subject.SubjectDetailsDto
import ch.learntrack.backend.common.LearnTrackConflictException
import ch.learntrack.backend.grade.GradeService
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.daos.GradeDao
import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.pojos.Grade
import ch.learntrack.backend.subject.SubjectService
import ch.learntrack.backend.user.UserService
import ch.learntrack.backend.utils.sanitizeInputString
import java.time.LocalDateTime
import java.util.UUID

public class AdminGradeService(
    private val gradeService: GradeService,
    private val userService: UserService,
    private val subjectService: SubjectService,
    private val gradeDao: GradeDao,
    private val userDao: UserDao,
) {
    public fun getAllGradesForSchool(schoolId: UUID): List<GradeInfoDto> =
        gradeService.getGradesBySchoolId(schoolId).map { grade ->
            GradeInfoDto(
                grades = grade,
                students = userService.getUsersByRoleAndGradeId(UserRole.STUDENT, grade.id),
                teachers = userService.getUsersByRoleAndGradeId(UserRole.TEACHER, grade.id),
            )
        }

    public fun createGrade(createGradeDto: CreateGradeDto) {
        if (gradeService.isGradeExistingInSchool(createGradeDto.name, createGradeDto.schoolId)) {
            throw LearnTrackConflictException(
                "Grade with name ${createGradeDto.name} already exists in school ${createGradeDto.schoolId}",
            )
        }

        val grade = Grade(
            id = UUID.randomUUID(),
            name = createGradeDto.name.trim().sanitizeInputString(),
            schoolId = createGradeDto.schoolId,
            created = LocalDateTime.now(),
            updated = LocalDateTime.now(),
        )

        gradeDao.insert(grade)
    }

    public fun getGradeDetails(gradeId: UUID): GradeDetailsDto = GradeDetailsDto(
        students = userService.getUsersByRoleAndGradeId(UserRole.STUDENT, gradeId),
        subjectDetailsDto = subjectService.getSubjectsByGradeId(gradeId).map { subject ->
            SubjectDetailsDto(
                subject = subject,
                teacher = subject.teacherId?.let { teacherId ->
                    userDao.fetchOneById(teacherId)?.let { teacher ->
                        userService.mapToDto(teacher)
                    }
                },
            )
        },
    )
}
