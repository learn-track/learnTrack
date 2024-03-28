package ch.learntrack.backend.backoffice.user

import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.references.USER

public fun UserDao.fetchAllAdminUsers(): List<User> = ctx()
    .select()
    .from(USER)
    .where(USER.USER_ROLE.eq(UserRole.ADMIN))
    .fetch()
    .into(User::class.java)
