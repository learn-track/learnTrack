package ch.learntrack.backend.grade

import ch.learntrack.backend.admin.subject.SubjectDetailsDto
import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.persistence.fetchByNameAndSchoolId
import ch.learntrack.backend.persistence.fetchGradesForUserByUserId
import ch.learntrack.backend.persistence.fetchSubjectsByGradeId
import ch.learntrack.backend.persistence.tables.daos.GradeDao
import ch.learntrack.backend.persistence.tables.pojos.Grade
import ch.learntrack.backend.persistence.tables.records.GradeRecord
import ch.learntrack.backend.user.UserService
import java.util.UUID

public class GradeService(
    private val gradeDao: GradeDao,
    private val userService: UserService,
) : EntityService<GradeDto, GradeRecord, Grade>(gradeDao) {
    public override fun mapToDto(pojo: Grade): GradeDto = GradeDto(
        id = pojo.id,
        name = pojo.name,
        schoolId = pojo.schoolId,
    )

    public fun getGradesForUserByUserId(userId: UUID): List<GradeDto> = gradeDao.fetchGradesForUserByUserId(userId).map(
        ::mapToDto,
    )

    public fun getGradesBySchoolId(schoolId: UUID): List<GradeDto> = gradeDao.fetchBySchoolId(schoolId).map(
        ::mapToDto)

    public fun getGradeByNameAndSchoolId(name: String, schoolId: UUID): Grade? = gradeDao.fetchByNameAndSchoolId(
        name,
        schoolId,
    )

    public fun isGradeExistingInSchool(name: String, schoolId: UUID): Boolean =
        getGradeByNameAndSchoolId(name, schoolId) != null

    public fun getSubjectDetailsByGradeId(gradeId: UUID): List<SubjectDetailsDto> =
        gradeDao.fetchSubjectsByGradeId(gradeId).map {
            SubjectDetailsDto(
                id = it.id,
                name = it.name,
                teacher = userService.getUserBySubjectId(it.id),
            )
        }
}
