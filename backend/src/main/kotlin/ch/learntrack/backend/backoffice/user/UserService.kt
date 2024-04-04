package ch.learntrack.backend.backoffice.user

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.records.UserRecord
import ch.learntrack.backend.security.PasswordService
import java.time.LocalDateTime
import java.util.UUID

public class UserService(
    private val userDao: UserDao,
    private val passwordService: PasswordService,
) : EntityService<UserDto, UserRecord, User>(userDao) {
    public override fun mapToDto(pojo: User): UserDto = UserDto(
        id = pojo.id,
        firstname = pojo.firstName,
        lastname = pojo.lastName,
        email = pojo.eMail,
        birthdate = pojo.birthdate,
        created = pojo.created,
        updated = pojo.updated,
    )

    public fun getAllAdminUsers(): List<UserDto> = userDao.fetchAllAdminUsers().map(::mapToDto)

    public fun createUser(createUserDto: CreateUserDto) {
        val user = User(
            id = UUID.randomUUID(),
            firstName = createUserDto.firstname,
            middleName = createUserDto.middlename,
            lastName = createUserDto.lastname,
            eMail = createUserDto.email,
            password = passwordService.createPasswordHash(createUserDto.password),
            userRole = UserRole.ADMIN,
            birthdate = null,
            created = LocalDateTime.now(),
            updated = LocalDateTime.now(),
        )
        userDao.insert(user)
    }
}
