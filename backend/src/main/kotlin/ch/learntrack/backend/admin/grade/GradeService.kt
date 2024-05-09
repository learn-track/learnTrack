package ch.learntrack.backend.admin.grade

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.grade.GradeDto
import ch.learntrack.backend.persistence.tables.daos.GradeDao
import ch.learntrack.backend.persistence.tables.pojos.Grade
import ch.learntrack.backend.persistence.tables.records.GradeRecord
import java.util.UUID

public class GradeService(private val gradeDao: GradeDao) : EntityService<GradeDto, GradeRecord, Grade>(gradeDao) {
    public override fun mapToDto(pojo: Grade): GradeDto = GradeDto(
        id = pojo.id,
        name = pojo.name,
        schoolId = pojo.schoolId,
    )

    public fun createGrade(createGradeDto: CreateGradeDto): Grade {
        if (gradeDao.fetchBySchoolId(createGradeDto.schoolId).any { it.name == createGradeDto.name.trim() }) {
            throw IllegalArgumentException(
                "Grade with name ${createGradeDto.name} already exists for school with id ${createGradeDto.schoolId}",
            )
        }

        val grade = Grade(
            id = UUID.randomUUID(),
            name = createGradeDto.name.trim(),
            schoolId = createGradeDto.schoolId,
        )

        gradeDao.insert(grade)

        return grade
    }
}
