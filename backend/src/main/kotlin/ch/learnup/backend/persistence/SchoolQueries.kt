package ch.learnup.backend.persistence

import ch.learnup.backend.persistence.tables.daos.SchoolDao
import ch.learnup.backend.persistence.tables.pojos.School
import ch.learnup.backend.persistence.tables.references.SCHOOL
import ch.learnup.backend.persistence.tables.references.USER
import ch.learnup.backend.persistence.tables.references.USER_SCHOOL
import java.util.UUID

fun SchoolDao.fetchGradeByUserId(userId: UUID): MutableList<School> = ctx()
    .select(SCHOOL)
    .from(USER_SCHOOL)
    .join(SCHOOL)
    .on(SCHOOL.ID.eq(USER_SCHOOL.SCHOOL_ID))
    .join(USER)
    .on(USER.ID.eq(USER_SCHOOL.USER_ID))
    .where(USER.ID.eq(userId))
    .fetch()
    .into(School::class.java)
