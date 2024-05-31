package ch.learntrack.backend.user

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.common.LearnTrackBadRequestException
import ch.learntrack.backend.common.LearnTrackConflictException
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.fetchAllUsersByRoleAndSchoolId
import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.records.UserRecord
import ch.learntrack.backend.persistence.tables.references.USER
import ch.learntrack.backend.security.PasswordService
import ch.learntrack.backend.utils.isEmailValid
import ch.learntrack.backend.utils.isPasswordValid
import ch.learntrack.backend.utils.sanitizeInputString
import java.time.LocalDateTime
import java.util.UUID

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

    public fun findUserByEmail(email: String): User? = userDao.fetchOne(USER.E_MAIL, email.lowercase())

    public fun getUsersByRoleAndSchoolId(userRole: UserRole, schoolId: UUID): List<UserDto> =
        userDao.fetchAllUsersByRoleAndSchoolId(userRole, schoolId).map(::mapToDto)

    public fun createUser(createUserDto: CreateUserDto): User {
        val emailLowerCase = createUserDto.email.trim().lowercase()

        if (!emailLowerCase.isEmailValid()) {
            throw LearnTrackBadRequestException("Email $emailLowerCase is not valid")
        }

        findUserByEmail(emailLowerCase)?.let {
            throw LearnTrackConflictException("Email $emailLowerCase already exists")
        }

        if (!createUserDto.password.isPasswordValid()) {
            throw LearnTrackBadRequestException(
                "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, " +
                        "one number and one special character",
            )
        }

        val user = User(
            id = UUID.randomUUID(),
            firstName = createUserDto.firstname.sanitizeInputString(),
            middleName = createUserDto.middlename?.sanitizeInputString(),
            lastName = createUserDto.lastname.sanitizeInputString(),
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
}
