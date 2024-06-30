package ch.learntrack.backend.backoffice.user

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.backoffice.BACKOFFICE_ROOT_URL
import ch.learntrack.backend.persistence.tables.references.USER
import ch.learntrack.backend.utils.createAdminUserFromTemplate
import ch.learntrack.backend.utils.createTeacherUserFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.setBasicAuthHeader
import ch.learntrack.backend.utils.userAdminTemplateId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.BodyInserters

class UserIntegrationTest: IntegrationTest() {
    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            userDao.insert(createAdminUserFromTemplate())
            userDao.insert(createTeacherUserFromTemplate(subjectId = null))
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
            .uri("$BACKOFFICE_ROOT_URL/user")
            .setBasicAuthHeader("wrong", "credentials")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `should return a list with only admin users`() {
        val response = webClient.get()
            .uri("$BACKOFFICE_ROOT_URL/user")
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectBodyList(UserDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response).hasSize(1)
        assertThat(response?.first()?.id).isEqualTo(userAdminTemplateId)
    }

    @Test
    fun `should be able to create a user`() {
        val validUserDto = CreateUserDto("John", "Doe", "test", "test.edu@gmail.com", "test" )

        webClient.post()
                .uri("$BACKOFFICE_ROOT_URL/user/create")
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

        val validUserDto1 = CreateUserDto("John2", null, "test2", "test2.edu@gmail.com", "test" )

        webClient.post()
                .uri("$BACKOFFICE_ROOT_URL/user/create")
                .body(BodyInserters.fromValue(validUserDto1))
                .setBasicAuthHeader(backendProperties)
                .exchange()
                .expectStatus()
                .isOk

        val user1 = userDao.fetchOne(USER.E_MAIL, validUserDto1.email)

        assertThat(user1).isNotNull
        assertThat(user1?.firstName).isEqualTo(validUserDto1.firstname)
        assertThat(user1?.middleName).isEqualTo(validUserDto1.middlename)
        assertThat(user1?.lastName).isEqualTo(validUserDto1.lastname)
        assertThat(user1?.password).isNotNull
        assertThat(user1?.password).isNotEqualTo(validUserDto1.password)
        assertThat(user1?.eMail).isEqualTo(validUserDto1.email)
    }

    @Test
    fun `should throw exception if email is invalid`() {
        val userDtoInvalidEmail = CreateUserDto("John", "Doe", "test", "test.edugmail.com", "test" )

        webClient.post()
                .uri("$BACKOFFICE_ROOT_URL/user/create")
                .body(BodyInserters.fromValue(userDtoInvalidEmail))
                .setBasicAuthHeader(backendProperties)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST)

        val user = userDao.fetchOne(USER.E_MAIL, userDtoInvalidEmail.email)

        assertThat(user).isNull()

        val userDtoInvalidEmail1 = CreateUserDto("John", "Doe", "test", "@gmail.com", "test" )

        webClient.post()
                .uri("$BACKOFFICE_ROOT_URL/user/create")
                .body(BodyInserters.fromValue(userDtoInvalidEmail1))
                .setBasicAuthHeader(backendProperties)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST)

        val userDtoInvalidEmail2 = CreateUserDto("John", "Doe", "test", "test.edu@gmail.", "test" )

        webClient.post()
                .uri("$BACKOFFICE_ROOT_URL/user/create")
                .body(BodyInserters.fromValue(userDtoInvalidEmail2))
                .setBasicAuthHeader(backendProperties)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST)

        val userDtoInvalidEmail3 = CreateUserDto("John", "Doe", "test", "testedu@gmailcom", "test" )

        webClient.post()
                .uri("$BACKOFFICE_ROOT_URL/user/create")
                .body(BodyInserters.fromValue(userDtoInvalidEmail3))
                .setBasicAuthHeader(backendProperties)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `should throw exception if email is already existing`() {
        val userDtoInvalidEmail3 = CreateUserDto("John", "Doe", "test", "testuser@gmail.com", "test" )

        webClient.post()
                .uri("$BACKOFFICE_ROOT_URL/user/create")
                .body(BodyInserters.fromValue(userDtoInvalidEmail3))
                .setBasicAuthHeader(backendProperties)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT)
    }

    @Test
    fun `should save uppercase email as lowercase on user creation`() {
        val validUser = CreateUserDto("John", "Doe", "test", "TESTLOWERCASE@GMAIL.COM", "test" )

        webClient.post()
                .uri("$BACKOFFICE_ROOT_URL/user/create")
                .body(BodyInserters.fromValue(validUser))
                .setBasicAuthHeader(backendProperties)
                .exchange()
                .expectStatus()
                .isOk

        val user = userDao.fetchOne(USER.E_MAIL, validUser.email.lowercase())
        assertThat(user).isNotNull
        assertThat(user?.eMail).isEqualTo("testlowercase@gmail.com")
    }

}
