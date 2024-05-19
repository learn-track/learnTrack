package ch.learntrack.backend.school

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.persistence.fetchSchoolsForUserByUserId
import ch.learntrack.backend.persistence.fetchUsersForSchoolBySchoolId
import ch.learntrack.backend.persistence.tables.daos.SchoolDao
import ch.learntrack.backend.persistence.tables.pojos.School
import ch.learntrack.backend.persistence.tables.records.SchoolRecord
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

    public fun hasUserAccessToSchool(userId: UUID, schoolId: UUID): Boolean =
        schoolDao.fetchUsersForSchoolBySchoolId(schoolId).any { it.id == userId }
}
