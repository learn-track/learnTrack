package ch.learnup.backend.user

import ch.learnup.backend.common.EntityService
import ch.learnup.backend.persistence.tables.daos.UserDao
import ch.learnup.backend.persistence.tables.pojos.User
import ch.learnup.backend.persistence.tables.records.UserRecord
import ch.learnup.backend.persistence.tables.references.USER

class UserService(private val userDao: UserDao) : EntityService<UserDto, UserRecord, User>(userDao) {
    public override fun mapToDto(pojo: User): UserDto = UserDto(
        id = pojo.id,
        email = pojo.eMail,
        firstname = pojo.firstName,
        middlename = pojo.middleName,
        lastname = pojo.lastName,
        userRole = pojo.userRole,
        birthDate = pojo.birthdate,
    )

    fun findUserByEmail(email: String) = userDao.fetchOne(USER.E_MAIL, email)
}
