package ch.learntrack.backend.admin

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.utils.setBasicAuthHeader
import org.junit.jupiter.api.Test

class AdminSecurityIntegrationTest: IntegrationTest() {
    @Test
    fun `disallow access to admin endpoints for users without admin role`() {
        webClient.post()
            .uri("/admin/createGrade")
            .setBasicAuthHeader("wrong", "credentials")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }
}