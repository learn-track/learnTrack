package ch.learntrack.backend.backoffice.school

import ch.learntrack.backend.persistence.tables.daos.SchoolDao
import ch.learntrack.backend.persistence.tables.pojos.School
import ch.learntrack.backend.persistence.tables.references.SCHOOL
import ch.learntrack.backend.persistence.tables.references.USER_SCHOOL
import java.util.UUID

public fun SchoolDao.fetchAllSchools(): MutableList<School> = ctx()
    .select()
    .from(SCHOOL)
    .fetch()
    .into(School::class.java)

public fun SchoolDao.fetchSchoolsByUserId(userId: UUID): List<School> = ctx()
    .select(SCHOOL)
    .from(USER_SCHOOL)
    .join(SCHOOL)
    .on(SCHOOL.ID.eq(USER_SCHOOL.SCHOOL_ID))
    .and(USER_SCHOOL.USER_ID.eq(userId))
    .fetch()
    .into(School::class.java)
