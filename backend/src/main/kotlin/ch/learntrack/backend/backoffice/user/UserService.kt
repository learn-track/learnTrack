package ch.learntrack.backend.backoffice.user

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.common.LearnTrackBadRequestException
import ch.learntrack.backend.common.LearnTrackConflictException
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.records.UserRecord
import ch.learntrack.backend.security.PasswordService
import ch.learntrack.backend.utils.isEmailValid
import ch.learntrack.backend.utils.sanitizeInputString
import java.time.LocalDateTime
import java.util.UUID

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
        val emailLowerCase = createUserDto.email.trim().lowercase()

        if (!emailLowerCase.isEmailValid()) {
            throw LearnTrackBadRequestException("Email $emailLowerCase is not valid")
        }

        if (isEmailExisting(emailLowerCase)) {
            throw LearnTrackConflictException("Email $emailLowerCase already exists")
        }

        val user = User(
            id = UUID.randomUUID(),
            firstName = createUserDto.firstname.trim().sanitizeInputString(),
            middleName = createUserDto.middlename?.trim()?.sanitizeInputString(),
            lastName = createUserDto.lastname.trim().sanitizeInputString(),
            eMail = emailLowerCase,
            password = passwordService.createPasswordHash(createUserDto.password),
            userRole = UserRole.ADMIN,
            birthdate = null,
            created = LocalDateTime.now(),
            updated = LocalDateTime.now(),
        )
        userDao.insert(user)
    }

    private fun isEmailExisting(email: String): Boolean = userDao.fetchByEMail(email).isNotEmpty()
}
