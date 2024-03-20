package ch.learnup.backend.grade

import ch.learnup.backend.common.EntityService
import ch.learnup.backend.persistence.fetchGradesForUserByUserId
import ch.learnup.backend.persistence.tables.daos.GradeDao
import ch.learnup.backend.persistence.tables.pojos.Grade
import ch.learnup.backend.persistence.tables.records.GradeRecord
import java.util.UUID

public class GradeService(private val gradeDao: GradeDao) : EntityService<GradeDto, GradeRecord, Grade>(gradeDao) {
    public override fun mapToDto(pojo: Grade): GradeDto = GradeDto(
        id = pojo.id,
        name = pojo.name,
        schoolId = pojo.schoolId,
    )

    public fun getGradesForUserByUserId(userId: UUID): List<GradeDto> = gradeDao.fetchGradesForUserByUserId(userId).map(
        ::mapToDto,
    )
}
