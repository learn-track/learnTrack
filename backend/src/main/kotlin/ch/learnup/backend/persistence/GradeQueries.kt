package ch.learnup.backend.persistence

import ch.learnup.backend.persistence.tables.daos.GradeDao
import ch.learnup.backend.persistence.tables.pojos.Grade
import ch.learnup.backend.persistence.tables.references.GRADE
import ch.learnup.backend.persistence.tables.references.USER
import ch.learnup.backend.persistence.tables.references.USER_GRADE
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
