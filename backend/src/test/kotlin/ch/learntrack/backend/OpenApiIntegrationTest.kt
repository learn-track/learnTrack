package ch.learntrack.backend

import org.junit.jupiter.api.Test

class OpenApiIntegrationTest : IntegrationTest() {
    @Test
    fun `should be able to display swagger UI`() {
        webClient.get()
            .uri("/swagger-ui/index.html")
            .exchange()
            .expectStatus()
            .isOk()
    }

    @Test
    fun `should generate open api spec`() {
        webClient.get()
            .uri("/openapi/v3/api-docs/learntrack-api")
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType("application/json")
    }
}