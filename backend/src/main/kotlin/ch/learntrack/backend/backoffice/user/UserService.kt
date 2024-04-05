package ch.learntrack.backend.backoffice.user

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.common.LearnTrackConflictException
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.records.UserRecord
import ch.learntrack.backend.security.PasswordService
import java.time.LocalDateTime
import java.util.UUID

private const val EMAIL_REGEX = """^[\w-.]+@([\w-]+\.)+[\w-]{2,4}${'$'}"""

public class UserService(
    private val userDao: UserDao,
    private val passwordService: PasswordService,
) : EntityService<UserDto, UserRecord, User>(userDao) {
    public override fun mapToDto(pojo: User): UserDto = UserDto(
        id = pojo.id,
        firstname = pojo.firstName,
        middlename = pojo.middleName,
        lastname = pojo.lastName,
        email = pojo.eMail,
        birthdate = pojo.birthdate,
        created = pojo.created,
        updated = pojo.updated,
    )

    public fun getAllAdminUsers(): List<UserDto> = userDao.fetchAllAdminUsers().map(::mapToDto)

    public fun createAdminUser(createUserDto: CreateUserDto) {
        if (!isEmailValid(createUserDto.email)) {
            throw LearnTrackConflictException("Invalid email")
        }

        if (isEmailExisting(createUserDto.email)) {
            throw LearnTrackConflictException("Email already exists")
        }

        val user = User(
            id = UUID.randomUUID(),
            firstName = createUserDto.firstname.trim(),
            middleName = createUserDto.middlename?.trim(),
            lastName = createUserDto.lastname.trim(),
            eMail = createUserDto.email.trim(),
            password = passwordService.createPasswordHash(createUserDto.password),
            userRole = UserRole.ADMIN,
            birthdate = null,
            created = LocalDateTime.now(),
            updated = LocalDateTime.now(),
        )
        userDao.insert(user)
    }

    private fun isEmailValid(email: String): Boolean = EMAIL_REGEX.toRegex().containsMatchIn(email)

    private fun isEmailExisting(email: String): Boolean = userDao.fetchByEMail(email).isNotEmpty()
}
