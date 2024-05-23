package ch.learntrack.backend.admin.grade

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.common.LearnTrackConflictException
import ch.learntrack.backend.grade.GradeDto
import ch.learntrack.backend.persistence.fetchByNameAndSchoolId
import ch.learntrack.backend.persistence.tables.daos.GradeDao
import ch.learntrack.backend.persistence.tables.pojos.Grade
import ch.learntrack.backend.persistence.tables.records.GradeRecord
import ch.learntrack.backend.utils.sanitizeInputString
import java.time.LocalDateTime
import java.util.UUID

public class GradeService(private val gradeDao: GradeDao) : EntityService<GradeDto, GradeRecord, Grade>(gradeDao) {
    public override fun mapToDto(pojo: Grade): GradeDto = GradeDto(
        id = pojo.id,
        name = pojo.name,
        schoolId = pojo.schoolId,
    )

    public fun createGrade(createGradeDto: CreateGradeDto): Grade {
        gradeDao.fetchByNameAndSchoolId(createGradeDto)?.let {
            throw LearnTrackConflictException(
                "Grade ${createGradeDto.name} already exists for school ${createGradeDto.schoolId}",
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

        return grade
    }
}
