package ch.learnup.backend.school

import ch.learnup.backend.common.EntityService
import ch.learnup.backend.persistence.fetchSchoolsForUserByUserId
import ch.learnup.backend.persistence.tables.daos.SchoolDao
import ch.learnup.backend.persistence.tables.pojos.School
import ch.learnup.backend.persistence.tables.records.SchoolRecord
import java.util.UUID

public class SchoolService(private val schoolDao: SchoolDao) : EntityService<SchoolDto, SchoolRecord, School>(
    schoolDao,
) {
    public override fun mapToDto(pojo: School): SchoolDto = SchoolDto(
        id = pojo.id,
        name = pojo.name,
        address = pojo.address,
        city = pojo.city,
        postcode = pojo.postcode,
    )

    public fun getSchoolsForUserByUserId(userId: UUID): List<SchoolDto> =
        schoolDao.fetchSchoolsForUserByUserId(userId).map(::mapToDto)
}
