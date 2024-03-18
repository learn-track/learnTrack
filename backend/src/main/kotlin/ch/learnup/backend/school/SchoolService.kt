package ch.learnup.backend.school

import ch.learnup.backend.common.EntityService
import ch.learnup.backend.persistence.fetchGradeByUserId
import ch.learnup.backend.persistence.tables.daos.SchoolDao
import ch.learnup.backend.persistence.tables.pojos.School
import ch.learnup.backend.persistence.tables.records.SchoolRecord
import java.util.UUID

class SchoolService(private val schoolDao: SchoolDao) : EntityService<SchoolDto, SchoolRecord, School>(schoolDao) {
    public override fun mapToDto(pojo: School): SchoolDto = SchoolDto(
        id = pojo.id,
        name = pojo.name,
        address = pojo.address,
        city = pojo.city,
        postcode = pojo.postcode,
    )

    fun getSchoolByUserId(userId: UUID) = schoolDao.fetchGradeByUserId(userId).map(::mapToDto)
}
