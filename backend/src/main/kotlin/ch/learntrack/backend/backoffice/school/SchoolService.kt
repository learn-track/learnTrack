package ch.learntrack.backend.backoffice.school

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.persistence.tables.daos.SchoolDao
import ch.learntrack.backend.persistence.tables.pojos.School
import ch.learntrack.backend.persistence.tables.records.SchoolRecord

public class SchoolService(
    private val schoolDao: SchoolDao,
) : EntityService<SchoolDto, SchoolRecord, School>(schoolDao) {
    public override fun mapToDto(pojo: School): SchoolDto = SchoolDto(
        id = pojo.id,
        name = pojo.name,
        address = pojo.address,
        city = pojo.city,
        postcode = pojo.postcode,
        created = pojo.created,
        updated = pojo.updated,
    )

    public fun getAllSchools(): List<SchoolDto> = schoolDao.fetchAllSchools().map(::mapToDto)
}
