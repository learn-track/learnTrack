package ch.learntrack.backend.user

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.common.LearnTrackBadRequestException
import ch.learntrack.backend.common.LearnTrackConflictException
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.fetchAllUsersByRoleAndGradeId
import ch.learntrack.backend.persistence.fetchAllUsersByRoleAndSchoolId
import ch.learntrack.backend.persistence.fetchUserBySubjectId
import ch.learntrack.backend.persistence.searchUserByUserRoleAndEmailSearchQuery
import ch.learntrack.backend.persistence.tables.daos.UserDao
import ch.learntrack.backend.persistence.tables.daos.UserGradeDao
import ch.learntrack.backend.persistence.tables.daos.UserSchoolDao
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.pojos.UserGrade
import ch.learntrack.backend.persistence.tables.pojos.UserSchool
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
    private val userSchoolDao: UserSchoolDao,
    private val userGradeDao: UserGradeDao,
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

    public fun getUsersByRoleAndGradeId(userRole: UserRole, gradeId: UUID): List<UserDto> =
        userDao.fetchAllUsersByRoleAndGradeId(userRole, gradeId).map(::mapToDto)

    public fun searchForUserByEmail(email: String, userRole: UserRole): List<User> =
        userDao.searchUserByUserRoleAndEmailSearchQuery(email, userRole)

    public fun getUserBySubjectId(subjectId: UUID): UserDto? =
        userDao.fetchUserBySubjectId(subjectId)?.let(::mapToDto)

    public fun createUser(
        createUserDto: CreateUserDto,
        userRole: UserRole,
        schoolId: UUID?,
        gradeId: UUID?,
    ): User {
        val emailToLowerCase = createUserDto.email.trim().lowercase()

        validateEmailAndPassword(emailToLowerCase, createUserDto.password)

        val user = User(
            id = UUID.randomUUID(),
            firstName = createUserDto.firstname.sanitizeInputString(),
            middleName = createUserDto.middlename?.sanitizeInputString(),
            lastName = createUserDto.lastname.sanitizeInputString(),
            eMail = emailToLowerCase,
            password = passwordService.createPasswordHash(createUserDto.password),
            userRole = userRole,
            birthdate = createUserDto.birthDate?.atTime(0, 0, 0, 0),
            created = LocalDateTime.now(),
            updated = LocalDateTime.now(),
        )
        userDao.insert(user)

        schoolId?.let {
            userSchoolDao.insert(UserSchool(userId = user.id, schoolId = it))
        }

        gradeId?.let {
            userGradeDao.insert(UserGrade(userId = user.id, gradeId = it))
        }
        return user
    }

    private fun validateEmailAndPassword(email: String, password: String) {
        if (!email.isEmailValid()) {
            throw LearnTrackBadRequestException("Email $email is not valid")
        }

        findUserByEmail(email)?.let {
            throw LearnTrackConflictException("Email $email already exists")
        }

        if (!password.isPasswordValid()) {
            throw LearnTrackBadRequestException(
                "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, " +
                        "one number and one special character",
            )
        }
    }
}
