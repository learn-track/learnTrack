package ch.learntrack.backend.user

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.common.LearnTrackConflictException
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.records.UserRecord
import ch.learntrack.backend.persistence.tables.references.USER
import ch.learntrack.backend.security.PasswordService
import java.time.LocalDateTime
import java.util.UUID

private const val EMAIL_REGEX = """^[\w-.]+@([\w-]+\.)+[\w-]{2,4}${'$'}"""
private const val PASSWORD_REGEX = """^(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\w\d\s:])([^\s]){8,}${'$'}"""

public class UserService(
    private val userDao: UserDao,
    private val passwordService: PasswordService,
) : EntityService<UserDto, UserRecord, User>(userDao) {
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

    public fun createUser(createUserDto: CreateUserDto): User {
        val emailLowerCase = createUserDto.email.trim().lowercase()

        if (!isEmailValid(emailLowerCase)) {
            throw LearnTrackConflictException("Invalid email $emailLowerCase")
        }

        findUserByEmail(emailLowerCase)?.let {
            throw LearnTrackConflictException("Email $emailLowerCase already exists")
        }

        if (!isPasswordValid(createUserDto.password)) {
            throw LearnTrackConflictException(
                "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, " +
                        "one number and one special character",
            )
        }

        val user = User(
            id = UUID.randomUUID(),
            firstName = createUserDto.firstname.trim(),
            middleName = createUserDto.middlename?.trim(),
            lastName = createUserDto.lastname.trim(),
            eMail = emailLowerCase,
            password = passwordService.createPasswordHash(createUserDto.password),
            userRole = UserRole.TEACHER,
            birthdate = null,
            created = LocalDateTime.now(),
            updated = LocalDateTime.now(),
        )
        userDao.insert(user)

        return user
    }

    private fun isEmailValid(email: String): Boolean = EMAIL_REGEX.toRegex().containsMatchIn(email)

    private fun isPasswordValid(password: String): Boolean = PASSWORD_REGEX.toRegex().containsMatchIn(password)
}
