package ch.learntrack.backend.persistence

import ch.learntrack.backend.persistence.tables.daos.GradeDao
import ch.learntrack.backend.persistence.tables.pojos.Grade
import ch.learntrack.backend.persistence.tables.references.GRADE
import ch.learntrack.backend.persistence.tables.references.USER
import ch.learntrack.backend.persistence.tables.references.USER_GRADE

import java.util.UUID

public fun GradeDao.fetchGradesForUserByUserId(userId: UUID): MutableList<Grade> = ctx()
    .select(GRADE)
    .from(USER_GRADE)
    .join(GRADE)
    .on(GRADE.ID.eq(USER_GRADE.GRADE_ID))
    .join(USER)
    .on(USER.ID.eq(USER_GRADE.USER_ID))
    .where(USER.ID.eq(userId))
    .fetch()
    .into(Grade::class.java)

public fun GradeDao.fetchByNameAndSchoolId(schoolId: UUID, name: String): Grade? = ctx()
    .select()
    .from(GRADE)
    .where(GRADE.SCHOOL_ID.eq(schoolId))
    .and(GRADE.NAME.eq(name))
    .fetchOneInto(Grade::class.java)
