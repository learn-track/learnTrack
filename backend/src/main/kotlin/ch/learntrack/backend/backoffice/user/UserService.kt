package ch.learntrack.backend.backoffice.user

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.records.UserRecord

public class UserService(private val userDao: UserDao) : EntityService<UserDto, UserRecord, User>(userDao) {
    public override fun mapToDto(pojo: User): UserDto = UserDto(
        id = pojo.id,
        firstname = pojo.firstName,
        lastname = pojo.lastName,
    )

    public fun getAllAdminUser(): List<UserDto> = userDao.fetchAllAdminUsers().map(::mapToDto)
}
