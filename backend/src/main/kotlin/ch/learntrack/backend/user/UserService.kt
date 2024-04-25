package ch.learntrack.backend.user

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.common.LearnTrackConflictException
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.records.UserRecord
import ch.learntrack.backend.persistence.tables.references.USER
import ch.learntrack.backend.security.PasswordService
import org.apache.commons.text.StringEscapeUtils
import java.time.LocalDateTime
import java.util.UUID

// Regular expression for email validation. It checks if the input is in the format of an email.
private const val EMAIL_REGEX = """^[\w-.]+@([\w-]+\.)+[\w-]{2,4}${'$'}"""

// Regular expression for password validation. It checks if the password contains at least one digit,
// one uppercase letter, one lowercase letter, one special character, and is at least 8 characters long.
private const val PASSWORD_REGEX = """^(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\w\d\s:])([^\s]){8,}${'$'}"""

// Regular expression to match HTML entities
private const val HTML_ENTITY_REGEX = """"&[^;]+;"""

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
            firstName = sanitizeInputString(createUserDto.firstname.trim()),
            middleName = createUserDto.middlename?.let { sanitizeInputString(it.trim()) },
            lastName = sanitizeInputString(createUserDto.lastname.trim()),
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

    private fun sanitizeInputString(input: String): String = StringEscapeUtils.escapeHtml4(
        StringEscapeUtils.escapeEcmaScript(input.replace("'", "''")),
    )
        .replace(HTML_ENTITY_REGEX.toRegex(), " ")
}
