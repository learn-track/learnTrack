package ch.learntrack.backend.user

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.records.UserRecord
import ch.learntrack.backend.persistence.tables.references.USER

public class UserService(private val userDao: UserDao) : EntityService<UserDto, UserRecord, User>(userDao) {
    public override fun mapToDto(pojo: User): UserDto = UserDto(
        id = pojo.id,
        email = pojo.eMail,
        firstname = pojo.firstName,
        middlename = pojo.middleName,
        lastname = pojo.lastName,
        userRole = pojo.userRole,
        birthDate = pojo.birthdate,
    )

    public fun findUserByEmail(email: String): User? = userDao.fetchOne(USER.E_MAIL, email)
}
