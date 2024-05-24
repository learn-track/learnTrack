package ch.learntrack.backend.user

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.utils.createAdminUserFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.userAdminTemplateId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.BodyInserters


class UserIntegrationTest: IntegrationTest() {
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
    fun `should return test string`() {
        webClient.get()
            .uri("/user/test")
            .headers { httpHeader -> httpHeader.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
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
}