package ch.learnup.backend.user

import ch.learnup.backend.IntegrationTest
import ch.learnup.backend.utils.createUserFromTemplate
import ch.learnup.backend.utils.deleteAll
import ch.learnup.backend.utils.runInTransaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.BodyInserters

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
}