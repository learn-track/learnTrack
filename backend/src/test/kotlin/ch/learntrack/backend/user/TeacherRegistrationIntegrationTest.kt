package ch.learntrack.backend.user

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.references.USER
import ch.learntrack.backend.utils.createAdminUserFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.sanitizeInputString
import ch.learntrack.backend.utils.setBasicAuthHeader
import ch.learntrack.backend.utils.userAdminTemplateId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.BodyInserters

class TeacherRegistrationIntegrationTest: IntegrationTest(){
    @BeforeEach
    fun seedDatabase() {
        transactionManager.runInTransaction {
            userDao.insert(createAdminUserFromTemplate())
        }
    }

    @AfterEach
    fun cleanUp() {
        transactionManager.runInTransaction {
            userDao.deleteAll()
        }
    }

    @Test
    fun `should return true if email does not exist`() {
        val email = "unusedEmail@gmail.com"

        webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/user/register/check-email-free")
                    .queryParam("email", email)
                    .build()
            }
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .isEqualTo(true)
    }

    @Test
    fun `should return false if email already exists`() {
        val user =  userDao.fetchOneById(userAdminTemplateId)

        assertThat(user).isNotNull

        webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/user/register/check-email-free")
                    .queryParam("email", user?.eMail)
                    .build()
            }
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Boolean::class.java)
            .isEqualTo(false)
    }

    @Test
    fun `should return jws token with successful registration`() {
        val createUserDto1 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "test.user@gmail.com",
            birthDate = null,
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
        assertThat(user1?.userRole).isEqualTo(UserRole.TEACHER)
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
            birthDate = null,
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
        assertThat(user2?.userRole).isEqualTo(UserRole.TEACHER)
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
            birthDate = null,
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
        assertThat(user3?.userRole).isEqualTo(UserRole.TEACHER)
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
            birthDate = null,
            password = "test!1234H*",
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidEmail))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        val user = userDao.fetchOne(USER.E_MAIL, userDtoInvalidEmail.email)

        assertThat(user).isNull()

        val userDtoInvalidEmail1 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "test.usergmail.com",
            birthDate = null,
            password = "test!1234H*",
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidEmail1))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        val userDtoInvalidEmail2 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "test.user@gmail.",
            birthDate = null,
            password = "test!1234H*",)

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidEmail2))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        val userDtoInvalidEmail3 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "testuser@gmailcom",
            birthDate = null,
            password = "test!1234H*",
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidEmail3))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `should throw exception if email is already existing`() {
        val userDtoEmailExisting = CreateUserDto("John", "Doe", "test", "testuser@gmail.com", null,"test")

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoEmailExisting))
            .setBasicAuthHeader("wrong", "credentials")
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
            birthDate = null,
            password = "test"
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidPassword1))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        //Password only text and lower case
        val userDtoInvalidPassword2 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user2.test@gmail.com",
            birthDate = null,
            password = "testdasdadad"
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidPassword2))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        //Password only text
        val userDtoInvalidPassword3 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user3.test@gmail.com",
            birthDate = null,
            password = "TestDddasdd"
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidPassword3))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        //Password has text, lower case, upper case, numbers but no special character
        val userDtoInvalidPassword4 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user4.test@gmail.com",
            birthDate = null,
            password = "TestF23213"
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidPassword4))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)

        //Password numbers, special characters but no text
        val userDtoInvalidPassword5 = CreateUserDto(
            firstname = "John",
            middlename = "Doe",
            lastname = "test",
            email = "user5.test@gmail.com",
            birthDate = null,
            password = "*%=)+23213"
        )

        webClient.post()
            .uri("/user/register")
            .body(BodyInserters.fromValue(userDtoInvalidPassword5))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `should sanitize input values`() {
        val createUserDto1 = CreateUserDto(
            firstname = "<script>alert('test')</script>",
            middlename = "don",
            lastname = "test",
            email = "user.test@gmail.com",
            birthDate = null,
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
        assertThat(user1?.userRole).isEqualTo(UserRole.TEACHER)
        assertThat(user1?.firstName).isEqualTo(createUserDto1.firstname.sanitizeInputString())
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
            birthDate = null,
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
        assertThat(user2?.userRole).isEqualTo(UserRole.TEACHER)
        assertThat(user2?.firstName).isEqualTo(createUserDto2.firstname)
        assertThat(user2?.middleName).isEqualTo(createUserDto2.middlename)
        assertThat(user2?.lastName).isEqualTo(createUserDto2.lastname.sanitizeInputString())
        assertThat(user2?.password).isNotNull
        assertThat(user2?.password).isNotEqualTo(createUserDto2.password)
        assertThat(user2?.eMail).isEqualTo(createUserDto2.email.lowercase())

        val createUserDto3 = CreateUserDto(
            firstname = "<div>bon</div>",
            middlename = "don",
            lastname = "von",
            email = "user3.test@gmail.com",
            birthDate = null,
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

        val user3 = userDao.fetchOne(USER.E_MAIL, createUserDto3.email)

        assertThat(user3).isNotNull
        assertThat(user3?.userRole).isEqualTo(UserRole.TEACHER)
        assertThat(user3?.firstName).isEqualTo(createUserDto3.firstname.sanitizeInputString())
        assertThat(user3?.middleName).isEqualTo(createUserDto3.middlename)
        assertThat(user3?.lastName).isEqualTo(createUserDto3.lastname)
        assertThat(user3?.password).isNotNull
        assertThat(user3?.password).isNotEqualTo(createUserDto3.password)
        assertThat(user3?.eMail).isEqualTo(createUserDto3.email.lowercase())
    }
}