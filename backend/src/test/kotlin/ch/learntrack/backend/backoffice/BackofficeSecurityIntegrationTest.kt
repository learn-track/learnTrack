package ch.learntrack.backend.backoffice

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.utils.setBasicAuthHeader
import org.junit.jupiter.api.Test

class BackofficeSecurityIntegrationTest: IntegrationTest() {

    @Test
    fun `should not be able to access backoffice endpoint as user`() {
        webClient.get()
            .uri("/backoffice/user")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `should not be able to access backoffice endpoints with wrong basic auth credentials`() {
        webClient.get()
            .uri("/backoffice/user")
            .setBasicAuthHeader("wrong", "credentials")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `should be able to access backoffice with correct credentials`() {
        webClient.get()
            .uri("/backoffice/user")
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun `should not be able to access backoffice school endpoint as normal user`() {
        webClient.get()
            .uri("/backoffice/school")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `should be able to access backoffice school endpoint with correct credentials`() {
        webClient.get()
            .uri("/backoffice/school")
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isOk
    }

}