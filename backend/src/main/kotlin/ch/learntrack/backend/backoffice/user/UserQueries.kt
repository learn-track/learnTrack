package ch.learntrack.backend.backoffice.user

import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.references.USER
import ch.learntrack.backend.persistence.tables.references.USER_SCHOOL
import java.util.UUID

public fun UserDao.fetchAllAdminUsers(): List<User> = ctx()
    .select()
    .from(USER)
    .where(USER.USER_ROLE.eq(UserRole.ADMIN))
    .fetch()
    .into(User::class.java)

public fun UserDao.fetchAllAdminUserBySchoolId(schoolId: UUID): List<User> = ctx()
    .select(USER)
    .from(USER_SCHOOL)
    .join(USER)
    .on(USER.ID.eq(USER_SCHOOL.USER_ID))
    .and(USER_SCHOOL.SCHOOL_ID.eq(schoolId))
    .where(USER.USER_ROLE.eq(UserRole.ADMIN))
    .fetch()
    .into(User::class.java)

public fun UserDao.fetchUserCountBySchoolIdAndUserRole(schoolId: UUID, userRole: UserRole): Int = ctx()
    .selectCount()
    .from(USER_SCHOOL)
    .join(USER)
    .on(USER.ID.eq(USER_SCHOOL.USER_ID))
    .where(USER_SCHOOL.SCHOOL_ID.eq(schoolId))
    .and(USER.USER_ROLE.eq(userRole))
    .fetchOne(0, Int::class.java)
    ?: 0
