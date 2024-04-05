package ch.learntrack.backend.backoffice.user

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.references.USER
import ch.learntrack.backend.utils.createUserFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.setBasicAuthHeader
import ch.learntrack.backend.utils.userTemplateId
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.BodyInserters

private const val TEACHER_USER = "5003e059-9cc0-4e86-a89d-9d64db1c1f1f"

class UserIntegrationTest: IntegrationTest() {

    private val teacherUserId = UUID.fromString(TEACHER_USER)

    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            userDao.insert(createUserFromTemplate())
            userDao.insert(createUserFromTemplate(id = teacherUserId, eMail = "teacher@gmail.com", userRole = UserRole.TEACHER))
        }
    }

    @AfterEach
    fun cleanUp() {
        transactionManager.runInTransaction {
            userDao.deleteAll()
        }
    }

    @Test
    fun `should get 401 with wrong credentials`() {
        webClient.get()
            .uri("/backoffice/user")
            .setBasicAuthHeader("wrong", "credentials")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `should return a list with only admin users`() {
        val response = webClient.get()
            .uri("/backoffice/user")
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectBodyList(UserDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response).hasSize(1)
        assertThat(response?.first()?.id).isEqualTo(userTemplateId)
    }

    @Test
    fun `should be able to create a user`() {
        val validUserDto = CreateUserDto("John", "Doe", "test", "test.edu@gmail.com", "test" )

        webClient.post()
                .uri("/backoffice/user/create")
                .body(BodyInserters.fromValue(validUserDto))
                .setBasicAuthHeader(backendProperties)
                .exchange()
                .expectStatus()
                .isOk

        val user = userDao.fetchOne(USER.E_MAIL, validUserDto.email)

        assertThat(user).isNotNull
        assertThat(user?.firstName).isEqualTo(validUserDto.firstname)
        assertThat(user?.middleName).isEqualTo(validUserDto.middlename)
        assertThat(user?.lastName).isEqualTo(validUserDto.lastname)
        assertThat(user?.password).isNotNull
        assertThat(user?.password).isNotSameAs(validUserDto.password)
        assertThat(user?.eMail).isEqualTo(validUserDto.email)

    }

    @Test
    fun `should throw exception if email is invalid`() {
        val userDtoInvalidEmail = CreateUserDto("John", "Doe", "test", "test.edugmail.com", "test" )

        webClient.post()
                .uri("/backoffice/user/create")
                .body(BodyInserters.fromValue(userDtoInvalidEmail))
                .setBasicAuthHeader(backendProperties)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT)

        val user = userDao.fetchOne(USER.E_MAIL, userDtoInvalidEmail.email)

        assertThat(user).isNull()

        val userDtoInvalidEmail1 = CreateUserDto("John", "Doe", "test", "@gmail.com", "test" )

        webClient.post()
                .uri("/backoffice/user/create")
                .body(BodyInserters.fromValue(userDtoInvalidEmail1))
                .setBasicAuthHeader(backendProperties)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT)

        val userDtoInvalidEmail2 = CreateUserDto("John", "Doe", "test", "test.edu@gmail.", "test" )

        webClient.post()
                .uri("/backoffice/user/create")
                .body(BodyInserters.fromValue(userDtoInvalidEmail2))
                .setBasicAuthHeader(backendProperties)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT)

        val userDtoInvalidEmail3 = CreateUserDto("John", "Doe", "test", "testedu@gmailcom", "test" )

        webClient.post()
                .uri("/backoffice/user/create")
                .body(BodyInserters.fromValue(userDtoInvalidEmail3))
                .setBasicAuthHeader(backendProperties)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT)
    }

    @Test
    fun `should throw exception if email is already existing`() {
        val userDtoInvalidEmail3 = CreateUserDto("John", "Doe", "test", "testuser@gmail.com", "test" )

        webClient.post()
                .uri("/backoffice/user/create")
                .body(BodyInserters.fromValue(userDtoInvalidEmail3))
                .setBasicAuthHeader(backendProperties)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT)
    }

}
