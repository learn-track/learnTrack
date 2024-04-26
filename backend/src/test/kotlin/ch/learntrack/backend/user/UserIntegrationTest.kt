package ch.learntrack.backend.user

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.persistence.tables.references.USER
import ch.learntrack.backend.utils.createUserFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.setBasicAuthHeader
import org.apache.commons.text.StringEscapeUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.BodyInserters
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus

// Regular expression to match HTML entities
private const val HTML_ENTITY_REGEX = """"&[a-z]+;"""

class UserIntegrationTest: IntegrationTest() {
    @BeforeEach
    fun seedDatabase() {
        transactionManager.runInTransaction {
            userDao.insert(createUserFromTemplate())
        }
    }

    @AfterEach
    fun cleanUp() {
        transactionManager.runInTransaction {
            userDao.deleteAll()
        }
    }

    @Test
    fun `should return test string`() {
        webClient.get()
            .uri("/user/test")
            .exchange()
            .expectBody()
            .jsonPath("$")
            .isEqualTo("test string from backend")
    }

    @Test
    fun `should not return data with invalid jws token`() {
        webClient.get()
            .uri("/user/test")
            .headers { httpHeader -> httpHeader.setBearerAuth("WrongToken") }
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `should return jws token with right credentials`() {
        val loginDto = LoginDto(
            email = "testuser@gmail.com",
            password = "test",
        )
        webClient.post()
            .uri("/user/login")
            .body(BodyInserters.fromValue(loginDto))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.token")
            .exists()
    }

    @Test
    fun `should not return jws token with wrong email`() {
        val loginDto = LoginDto(
            email = "wrong@gmail.com",
            password = "test",
        )
        webClient.post()
            .uri("/user/login")
            .body(BodyInserters.fromValue(loginDto))
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `should not return jws token with wrong password`() {
        val loginDto = LoginDto(
            email = "testuser@gmail.com",
            password = "wrong password",
        )
        webClient.post()
            .uri("/user/login")
            .body(BodyInserters.fromValue(loginDto))
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `should return jws token with uppercase login email`() {
        val loginDto = LoginDto(
                email = "TESTUSER@GMAIL.COM",
                password = "test",
        )
        webClient.post()
                .uri("/user/login")
                .body(BodyInserters.fromValue(loginDto))
                .exchange()
                .expectStatus()
                .isOk
                .expectBody()
                .jsonPath("$.token")
                .exists()
    }

    @Test
    fun `should return jws token with successful registration`() {
        val createUserDto1 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "test.user@gmail.com",
            password = "test!1234H*",
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(createUserDto1))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.token")
            .exists()

        val user1 = userDao.fetchOne(USER.E_MAIL, createUserDto1.email)

        assertThat(user1).isNotNull
        assertThat(user1?.firstName).isEqualTo(createUserDto1.firstname)
        assertThat(user1?.middleName).isEqualTo(createUserDto1.middlename)
        assertThat(user1?.lastName).isEqualTo(createUserDto1.lastname)
        assertThat(user1?.password).isNotNull
        assertThat(user1?.password).isNotEqualTo(createUserDto1.password)
        assertThat(user1?.eMail).isEqualTo(createUserDto1.email)

        val createUserDto2 = CreateUserDto(
            firstname = "Test",
            middlename = null,
            lastname = "test",
            email = "user.test@gmail.com",
            password = "test!1234H*",
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(createUserDto2))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.token")
            .exists()

        val user2 = userDao.fetchOne(USER.E_MAIL, createUserDto2.email)

        assertThat(user2).isNotNull
        assertThat(user2?.firstName).isEqualTo(createUserDto2.firstname)
        assertThat(user2?.middleName).isEqualTo(createUserDto2.middlename)
        assertThat(user2?.lastName).isEqualTo(createUserDto2.lastname)
        assertThat(user2?.password).isNotNull
        assertThat(user2?.password).isNotEqualTo(createUserDto2.password)
        assertThat(user2?.eMail).isEqualTo(createUserDto2.email)

        val createUserDto3 = CreateUserDto(
            firstname = "Test",
            middlename = null,
            lastname = "bene",
            email = "BENEDICT-EDU@GMAIL.COM",
            password = "test!1234H*",
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(createUserDto3))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.token")
            .exists()

        val user3 = userDao.fetchOne(USER.E_MAIL, createUserDto3.email.lowercase())

        assertThat(user3).isNotNull
        assertThat(user3?.firstName).isEqualTo(createUserDto3.firstname)
        assertThat(user3?.middleName).isEqualTo(createUserDto3.middlename)
        assertThat(user3?.lastName).isEqualTo(createUserDto3.lastname)
        assertThat(user3?.password).isNotNull
        assertThat(user3?.password).isNotEqualTo(createUserDto3.password)
        assertThat(user3?.eMail).isEqualTo(createUserDto3.email.lowercase())
    }

    @Test
    fun `should throw exception if email is invalid`() {
        val userDtoInvalidEmail = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "@gmail.com",
            password = "test!1234H*",
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidEmail))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)

        val user = userDao.fetchOne(USER.E_MAIL, userDtoInvalidEmail.email)

        assertThat(user).isNull()

        val userDtoInvalidEmail1 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "test.usergmail.com",
            password = "test!1234H*",
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidEmail1))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)

        val userDtoInvalidEmail2 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "test.user@gmail.",
            password = "test!1234H*",)

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidEmail2))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)

        val userDtoInvalidEmail3 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "testuser@gmailcom",
            password = "test!1234H*",
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidEmail3))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)
    }

    @Test
    fun `should throw exception if email is already existing`() {
        val userDtoEmailExisting = CreateUserDto("John", "Doe", "test", "testuser@gmail.com", "test")

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoEmailExisting))
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)
    }

    @Test
    fun `should throw exception if password is invalid`() {
        //Password to short and only text and lower case
        val userDtoInvalidPassword1 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user.test@gmail.com",
            password = "test"
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidPassword1))
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)

        //Password only text and lower case
        val userDtoInvalidPassword2 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user2.test@gmail.com",
            password = "testdasdadad"
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidPassword2))
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)

        //Password only text
        val userDtoInvalidPassword3 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user3.test@gmail.com",
            password = "TestDddasdd"
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidPassword3))
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)

        //Password has text, lower case, upper case, numbers but no special character
        val userDtoInvalidPassword4 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user4.test@gmail.com",
            password = "TestF23213"
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidPassword4))
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)

        //Password numbers, special characters but no text
        val userDtoInvalidPassword5 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user5.test@gmail.com",
            password = "*%=)+23213"
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidPassword5))
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)
    }

    @Test
    fun `should sanitize input values`() {
        val createUserDto1 = CreateUserDto(
            firstname = "<script>alert('test')</script>",
            middlename = "don",
            lastname = "test",
            email = "user.test@gmail.com",
            password = "test!1234H*",
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(createUserDto1))
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.token")
            .exists()

        val user1 = userDao.fetchOne(USER.E_MAIL, createUserDto1.email)

        assertThat(user1).isNotNull
        assertThat(user1?.firstName).isEqualTo(sanitizeInputString(createUserDto1.firstname))
        assertThat(user1?.middleName).isEqualTo(createUserDto1.middlename)
        assertThat(user1?.lastName).isEqualTo(createUserDto1.lastname)
        assertThat(user1?.password).isNotNull
        assertThat(user1?.password).isNotEqualTo(createUserDto1.password)
        assertThat(user1?.eMail).isEqualTo(createUserDto1.email.lowercase())

        val createUserDto2 = CreateUserDto(
            firstname = "bon",
            middlename = "don",
            lastname = "SELECT * FROM users WHERE user.name = 'dasdad';",
            email = "user2.test@gmail.com",
            password = "test!1234H*",
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(createUserDto2))
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.token")
            .exists()

        val user2 = userDao.fetchOne(USER.E_MAIL, createUserDto2.email)

        assertThat(user2).isNotNull
        assertThat(user2?.firstName).isEqualTo(createUserDto2.firstname)
        assertThat(user2?.middleName).isEqualTo(createUserDto2.middlename)
        assertThat(user2?.lastName).isEqualTo(sanitizeInputString(createUserDto2.lastname))
        assertThat(user2?.password).isNotNull
        assertThat(user2?.password).isNotEqualTo(createUserDto2.password)
        assertThat(user2?.eMail).isEqualTo(createUserDto2.email.lowercase())

        val createUserDto3 = CreateUserDto(
            firstname = "<div>bon</div>",
            middlename = "don",
            lastname = "von",
            email = "user3.test@gmail.com",
            password = "test!1234H*",
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(createUserDto3))
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.token")
            .exists()

        val user3 = userDao.fetchOne(USER.E_MAIL, createUserDto3.email)

        assertThat(user3).isNotNull
        assertThat(user3?.firstName).isEqualTo(sanitizeInputString(createUserDto3.firstname))
        assertThat(user3?.middleName).isEqualTo(createUserDto3.middlename)
        assertThat(user3?.lastName).isEqualTo(createUserDto3.lastname)
        assertThat(user3?.password).isNotNull
        assertThat(user3?.password).isNotEqualTo(createUserDto3.password)
        assertThat(user3?.eMail).isEqualTo(createUserDto3.email.lowercase())
    }

    private fun sanitizeInputString(input: String): String = StringEscapeUtils.escapeHtml4(
        StringEscapeUtils.escapeEcmaScript(input.replace("'", "''").replace(HTML_ENTITY_REGEX.toRegex(), " ")),
    )
}