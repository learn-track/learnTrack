package ch.learntrack.backend.persistence

import ch.learntrack.backend.persistence.tables.daos.SchoolDao
import ch.learntrack.backend.persistence.tables.pojos.School
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.references.SCHOOL
import ch.learntrack.backend.persistence.tables.references.USER
import ch.learntrack.backend.persistence.tables.references.USER_SCHOOL
import java.util.UUID

public fun SchoolDao.fetchSchoolsForUserByUserId(userId: UUID): MutableList<School> = ctx()
    .select(SCHOOL)
    .from(USER_SCHOOL)
    .join(SCHOOL)
    .on(SCHOOL.ID.eq(USER_SCHOOL.SCHOOL_ID))
    .join(USER)
    .on(USER.ID.eq(USER_SCHOOL.USER_ID))
    .where(USER.ID.eq(userId))
    .fetch()
    .into(School::class.java)

public fun SchoolDao.fetchUsersForSchoolBySchoolId(schoolId: UUID): MutableList<User> = ctx()
    .select(USER)
    .from(USER_SCHOOL)
    .join(SCHOOL)
    .on(USER_SCHOOL.SCHOOL_ID.eq(SCHOOL.ID))
    .join(USER)
    .on(USER_SCHOOL.USER_ID.eq(USER.ID))
    .where(SCHOOL.ID.eq(schoolId))
    .fetch()
    .into(User::class.java)
