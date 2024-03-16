package ch.learnup.backend.user

import ch.learnup.backend.IntegrationTest
import org.junit.jupiter.api.Test

class UserIntegrationTest: IntegrationTest() {
    @Test
    fun `should return test string`() {
        webClient.get()
            .uri("/user")
            .exchange()
            .expectBody()
            .jsonPath("$.name")
            .isEqualTo("test string from backend")
    }
}