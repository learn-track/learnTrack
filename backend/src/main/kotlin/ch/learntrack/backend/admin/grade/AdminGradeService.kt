package ch.learntrack.backend.admin.grade

import ch.learntrack.backend.common.LearnTrackConflictException
import ch.learntrack.backend.grade.CreateGradeDto
import ch.learntrack.backend.grade.GradeDto
import ch.learntrack.backend.grade.GradeService
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.daos.GradeDao
import ch.learntrack.backend.persistence.tables.pojos.Grade
import ch.learntrack.backend.user.UserService
import ch.learntrack.backend.utils.sanitizeInputString
import java.time.LocalDateTime
import java.util.UUID

public class AdminGradeService(
    private val gradeService: GradeService,
    private val userService: UserService,
    private val gradeDao: GradeDao,
) {
    public fun getGrades(schoolId: UUID): GradeDetailsDto = GradeDetailsDto(
        grades = gradeService.getGradesBySchoolId(schoolId),
        users = userService.getUsersByRoleAndSchoolId(UserRole.STUDENT, schoolId),
    )

    public fun createGrade(createGradeDto: CreateGradeDto): GradeDto {
        if (gradeService.isGradeNameExistingInThisSchool(createGradeDto)) {
            throw LearnTrackConflictException("Grade with name ${createGradeDto.name} already exists in this school")
        }

        val grade = Grade(
            id = UUID.randomUUID(),
            name = createGradeDto.name.trim().sanitizeInputString(),
            schoolId = createGradeDto.schoolId,
            created = LocalDateTime.now(),
            updated = LocalDateTime.now(),
        )

        gradeDao.insert(grade)

        return gradeService.mapToDto(grade)
    }
}
