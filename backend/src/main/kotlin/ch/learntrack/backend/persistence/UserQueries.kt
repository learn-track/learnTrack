package ch.learntrack.backend.persistence

import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.references.USER
import ch.learntrack.backend.persistence.tables.references.USER_SCHOOL
import java.util.UUID

public fun UserDao.fetchAllUsersByRoleAndSchoolId(userRole: UserRole, schoolId: UUID): List<User> = ctx()
    .select()
    .from(USER)
    .leftJoin(USER_SCHOOL)
    .on(USER.ID.eq(USER_SCHOOL.USER_ID))
    .and(USER_SCHOOL.SCHOOL_ID.eq(schoolId))
    .where(USER.USER_ROLE.eq(userRole))
    .fetch()
    .into(User::class.java)
