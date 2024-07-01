package ch.learntrack.backend.backoffice.school

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.common.LearnTrackConflictException
import ch.learntrack.backend.persistence.tables.daos.SchoolDao
import ch.learntrack.backend.persistence.tables.pojos.School
import ch.learntrack.backend.persistence.tables.records.SchoolRecord
import ch.learntrack.backend.utils.sanitizeInputString
import java.time.LocalDateTime
import java.util.UUID

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

    public fun fetchSchoolsByUserId(userId: UUID): List<SchoolDto> = schoolDao.fetchSchoolsByUserId(userId).map(
        ::mapToDto,
    )

    public fun createSchool(createSchoolDto: CreateSchoolDto) {
        if (isSchoolExisting(createSchoolDto)) {
            throw LearnTrackConflictException("School already exists")
        }

        val school = School(
            id = UUID.randomUUID(),
            name = createSchoolDto.name.trim().sanitizeInputString(),
            address = createSchoolDto.address.trim().sanitizeInputString(),
            city = createSchoolDto.city.trim(),
            postcode = createSchoolDto.postcode,
            created = LocalDateTime.now(),
            updated = LocalDateTime.now(),
        )
        schoolDao.insert(school)
    }

    private fun isSchoolExisting(createSchoolDto: CreateSchoolDto): Boolean = schoolDao.fetchByName(
        createSchoolDto.name.trim(),
    ).any {
        it.city.trim() == createSchoolDto.city.trim() && it.postcode == createSchoolDto.postcode
    }
}
