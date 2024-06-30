package ch.learntrack.backend.admin.student

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.admin.ADMIN_ROOT_URL
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.references.USER
import ch.learntrack.backend.persistence.tables.references.USER_GRADE
import ch.learntrack.backend.persistence.tables.references.USER_SCHOOL
import ch.learntrack.backend.utils.createAdminUserFromTemplate
import ch.learntrack.backend.utils.createGradeFromTemplate
import ch.learntrack.backend.utils.createSchoolFromTemplate
import ch.learntrack.backend.utils.createStudentUserFromTemplate
import ch.learntrack.backend.utils.createUserGradeFromTemplate
import ch.learntrack.backend.utils.createUserSchoolFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.gradeTemplateId
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.sanitizeInputString
import ch.learntrack.backend.utils.schoolTemplateId
import ch.learntrack.backend.utils.userAdminTemplateId
import ch.learntrack.backend.utils.userStudentTemplateId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.BodyInserters
import java.time.LocalDate
import java.util.UUID

private const val USER_WITH_STUDENT_ROLE = "40d8b918-8f50-4b32-a3f5-4548d7883c55"
private const val USER_NOT_ASSIGNED_TO_SCHOOL = "40d8b918-8f50-4b32-a3f5-4548d7883c54"

class StudentIntegrationTest : IntegrationTest() {

    private val userIdWithStudentRole = UUID.fromString(USER_WITH_STUDENT_ROLE)
    private val userNotAssignedToSchoolId = UUID.fromString(USER_NOT_ASSIGNED_TO_SCHOOL)

    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            schoolDao.insert(createSchoolFromTemplate())
            gradeDao.insert(createGradeFromTemplate())
            userDao.insert(createAdminUserFromTemplate())
            userDao.insert(createStudentUserFromTemplate())
            userDao.insert(createStudentUserFromTemplate
                (
                id = userIdWithStudentRole,
                firstName = "studentUser",
                lastName = "test",
                eMail = "studentUser.test@gmail.com"
            ))
            userDao.insert(createAdminUserFromTemplate
                (
                id = userNotAssignedToSchoolId,
                firstName = "testAdmin",
                lastName = "user2",
                eMail = "testAdminuser@gmail.com",
            ))
            userSchoolDao.insert(createUserSchoolFromTemplate(userAdminTemplateId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userStudentTemplateId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userIdWithStudentRole))
            userGradeDao.insert(createUserGradeFromTemplate(userStudentTemplateId))
            userGradeDao.insert(createUserGradeFromTemplate(userIdWithStudentRole))
        }
    }

    @AfterEach
    fun cleanUp() {
        transactionManager.runInTransaction {
            schoolDao.deleteAll()
            userDao.deleteAll()
            userSchoolDao.deleteAll()
        }
    }

    @Test
    fun `should get StudentDetailsDto for assigned school`() {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(StudentDetailsDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response).hasSize(2)
        assertThat(response)
            .anyMatch { it.grade.id == gradeTemplateId }
            .noneMatch { it.user.id == userNotAssignedToSchoolId }
    }

    @Test
    fun `should not get StudentDetailsDto if user not assigned to school`() {
        webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/grade")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userNotAssignedToSchoolId)) }
            .exchange()
            .expectStatus()
            .isForbidden
    }

    @Test
    fun `should throw exception if email is invalid`() {
        val studentDtoInvalidEmail = CreateStudentDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "@gmail.com",
            birthDate = LocalDate.now(),
            password = "test!1234H*",
            schoolId = schoolTemplateId,
            gradeId = gradeTemplateId,
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student/createStudent")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(studentDtoInvalidEmail))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        val user = userDao.fetchOne(USER.E_MAIL, studentDtoInvalidEmail.email)

        assertThat(user).isNull()

        val studentDtoInvalidEmail1 = CreateStudentDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "test.usergmail.com",
            birthDate = LocalDate.now(),
            password = "test!1234H*",
            schoolId = schoolTemplateId,
            gradeId = gradeTemplateId,
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student/createStudent")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(studentDtoInvalidEmail1))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        val studentDtoInvalidEmail2 = CreateStudentDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "test.user@gmail.",
            birthDate = LocalDate.now(),
            password = "test!1234H*",
            schoolId = schoolTemplateId,
            gradeId = gradeTemplateId,
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student/createStudent")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(studentDtoInvalidEmail2))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        val studentDtoInvalidEmail3 = CreateStudentDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "testuser@gmailcom",
            birthDate = LocalDate.now(),
            password = "test!1234H*",
            schoolId = schoolTemplateId,
            gradeId = gradeTemplateId,
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student/createStudent")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(studentDtoInvalidEmail3))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `should throw exception if email is already existing`() {
        val createStudentDtoEmailExisting = CreateStudentDto(
            "John",
            "Doe",
            "test",
            "testuser@gmail.com",
            LocalDate.now(),
            "test",
            schoolId = schoolTemplateId,
            gradeTemplateId
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student/createStudent")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(createStudentDtoEmailExisting))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)
    }

    @Test
    fun `should throw exception if password is invalid`() {
        //Password to short and only text and lower case
        val createStudentDtoInvalidPassword1 = CreateStudentDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user.test@gmail.com",
            birthDate = LocalDate.now(),
            password = "test",
            schoolId = schoolTemplateId,
            gradeId = gradeTemplateId,
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student/createStudent")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(createStudentDtoInvalidPassword1))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        //Password only text and lower case
        val createStudentDtoInvalidPassword2 = CreateStudentDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user2.test@gmail.com",
            birthDate = LocalDate.now(),
            password = "testdasdadad",
            schoolId = schoolTemplateId,
            gradeId = gradeTemplateId,
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student/createStudent")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(createStudentDtoInvalidPassword2))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        //Password only text
        val createStudentDtoInvalidPassword3 = CreateStudentDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user3.test@gmail.com",
            birthDate = LocalDate.now(),
            password = "TestDddasdd",
            schoolId = schoolTemplateId,
            gradeId = gradeTemplateId,
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student/createStudent")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(createStudentDtoInvalidPassword3))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        //Password has text, lower case, upper case, numbers but no special character
        val createStudentDtoInvalidPassword4 = CreateStudentDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user4.test@gmail.com",
            birthDate = LocalDate.now(),
            password = "TestF23213",
            schoolId = schoolTemplateId,
            gradeId = gradeTemplateId,
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student/createStudent")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(createStudentDtoInvalidPassword4))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        //Password numbers, special characters but no text
        val createStudentDtoInvalidPassword5 = CreateStudentDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user5.test@gmail.com",
            birthDate = LocalDate.now(),
            password = "*%=)+23213",
            schoolId = schoolTemplateId,
            gradeId = gradeTemplateId,
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student/createStudent")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(createStudentDtoInvalidPassword5))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `should sanitize input values`() {
        val createStudentDto1 = CreateStudentDto(
            firstname = "<script>alert('test')</script>",
            middlename = "don",
            lastname = "test",
            email = "user.test@gmail.com",
            birthDate = LocalDate.now(),
            password = "test!1234H*",
            schoolId = schoolTemplateId,
            gradeId = gradeTemplateId,
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student/createStudent")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(createStudentDto1))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.OK)

        val user1 = userDao.fetchOne(USER.E_MAIL, createStudentDto1.email)
        val school1 = userSchoolDao.fetchOne(USER_SCHOOL.USER_ID, user1?.id)
        val grade1 = userGradeDao.fetchOne(USER_GRADE.USER_ID, user1?.id)

        assertThat(user1).isNotNull
        assertThat(school1).isNotNull
        assertThat(grade1).isNotNull
        assertThat(school1?.schoolId).isEqualTo(schoolTemplateId)
        assertThat(grade1?.gradeId).isEqualTo(gradeTemplateId)
        assertThat(user1?.userRole).isEqualTo(UserRole.STUDENT)
        assertThat(user1?.firstName).isEqualTo(createStudentDto1.firstname.sanitizeInputString())
        assertThat(user1?.middleName).isEqualTo(createStudentDto1.middlename)
        assertThat(user1?.lastName).isEqualTo(createStudentDto1.lastname)
        assertThat(user1?.password).isNotNull
        assertThat(user1?.password).isNotEqualTo(createStudentDto1.password)
        assertThat(user1?.eMail).isEqualTo(createStudentDto1.email.lowercase())

        val createStudentDto2 = CreateStudentDto(
            firstname = "bon",
            middlename = "don",
            lastname = "SELECT * FROM users WHERE user.name = 'dasdad';",
            email = "user2.test@gmail.com",
            birthDate = LocalDate.now(),
            password = "test!1234H*",
            schoolId = schoolTemplateId,
            gradeId = gradeTemplateId,
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student/createStudent")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(createStudentDto2))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.OK)

        val user2 = userDao.fetchOne(USER.E_MAIL, createStudentDto2.email)
        val school2 = userSchoolDao.fetchOne(USER_SCHOOL.USER_ID, user2?.id)
        val grade2 = userGradeDao.fetchOne(USER_GRADE.USER_ID, user2?.id)

        assertThat(user2).isNotNull
        assertThat(school2).isNotNull
        assertThat(grade2).isNotNull
        assertThat(school2?.schoolId).isEqualTo(schoolTemplateId)
        assertThat(grade2?.gradeId).isEqualTo(gradeTemplateId)
        assertThat(user2?.userRole).isEqualTo(UserRole.STUDENT)
        assertThat(user2?.firstName).isEqualTo(createStudentDto2.firstname)
        assertThat(user2?.middleName).isEqualTo(createStudentDto2.middlename)
        assertThat(user2?.lastName).isEqualTo(createStudentDto2.lastname.sanitizeInputString())
        assertThat(user2?.password).isNotNull
        assertThat(user2?.password).isNotEqualTo(createStudentDto2.password)
        assertThat(user2?.eMail).isEqualTo(createStudentDto2.email.lowercase())

        val createStudentDto3 = CreateStudentDto(
            firstname = "<div>bon</div>",
            middlename = "don",
            lastname = "von",
            email = "user3.test@gmail.com",
            birthDate = LocalDate.now(),
            password = "test!1234H*",
            schoolId = schoolTemplateId,
            gradeId = gradeTemplateId,
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student/createStudent")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(createStudentDto3))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.OK)

        val user3 = userDao.fetchOne(USER.E_MAIL, createStudentDto3.email)
        val school3 = userSchoolDao.fetchOne(USER_SCHOOL.USER_ID, user3?.id)
        val grade3 = userGradeDao.fetchOne(USER_GRADE.USER_ID, user3?.id)

        assertThat(user3).isNotNull
        assertThat(school3).isNotNull
        assertThat(grade3).isNotNull
        assertThat(school3?.schoolId).isEqualTo(schoolTemplateId)
        assertThat(grade3?.gradeId).isEqualTo(gradeTemplateId)
        assertThat(user3?.userRole).isEqualTo(UserRole.STUDENT)
        assertThat(user3?.firstName).isEqualTo(createStudentDto3.firstname.sanitizeInputString())
        assertThat(user3?.middleName).isEqualTo(createStudentDto3.middlename)
        assertThat(user3?.lastName).isEqualTo(createStudentDto3.lastname)
        assertThat(user3?.password).isNotNull
        assertThat(user3?.password).isNotEqualTo(createStudentDto3.password)
        assertThat(user3?.eMail).isEqualTo(createStudentDto3.email.lowercase())
    }
}