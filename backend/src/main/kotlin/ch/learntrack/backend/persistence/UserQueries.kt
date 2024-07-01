package ch.learntrack.backend.persistence

import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.references.SUBJECT
import ch.learntrack.backend.persistence.tables.references.USER
import ch.learntrack.backend.persistence.tables.references.USER_GRADE
import ch.learntrack.backend.persistence.tables.references.USER_SCHOOL
import java.util.UUID

public fun UserDao.fetchAllUsersByRoleAndSchoolId(userRole: UserRole, schoolId: UUID): List<User> = ctx()
    .select()
    .from(USER)
    .join(USER_SCHOOL)
    .on(USER.ID.eq(USER_SCHOOL.USER_ID))
    .and(USER_SCHOOL.SCHOOL_ID.eq(schoolId))
    .where(USER.USER_ROLE.eq(userRole))
    .fetch()
    .into(User::class.java)

public fun UserDao.fetchAllUsersByRoleAndGradeId(userRole: UserRole, gradeId: UUID): List<User> = ctx()
    .select()
    .from(USER)
    .join(USER_GRADE)
    .on(USER.ID.eq(USER_GRADE.USER_ID))
    .and(USER_GRADE.GRADE_ID.eq(gradeId))
    .where(USER.USER_ROLE.eq(userRole))
    .fetch()
    .into(User::class.java)

public fun UserDao.searchUserByUserRoleAndEmailSearchQuery(email: String, userRole: UserRole): List<User> = ctx()
    .select()
    .from(USER)
    .where(USER.E_MAIL.likeIgnoreCase("%$email%"))
    .and(USER.USER_ROLE.eq(userRole))
    .fetch()
    .into(User::class.java)

public fun UserDao.fetchUserBySubjectId(subjectId: UUID): User? = ctx()
    .select()
    .from(USER)
    .join(SUBJECT)
    .on(USER.ID.eq(SUBJECT.TEACHER_ID))
    .where(SUBJECT.ID.eq(subjectId))
    .fetchOneInto(User::class.java)
