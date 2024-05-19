package ch.learntrack.backend.backoffice

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.utils.setBasicAuthHeader
import org.junit.jupiter.api.Test


class BackofficeSecurityIntegrationTest: IntegrationTest() {
    @Test
    fun `should not be able to access backoffice endpoint as admin user`() {
        webClient.get()
            .uri(BACKOFFICE_ROOT_URL)
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `should not be able to access backoffice endpoints with wrong basic auth credentials`() {
        webClient.get()
            .uri(BACKOFFICE_ROOT_URL)
            .setBasicAuthHeader("wrong", "credentials")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }
}