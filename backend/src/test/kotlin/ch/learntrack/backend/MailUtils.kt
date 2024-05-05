package ch.learntrack.backend

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody


@JsonIgnoreProperties(ignoreUnknown = true)
data class MultipartEmail(
    val html: String,
    val subject: String,
    val to: SmtpEmailParticipants,
    val from: SmtpEmailParticipants,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SmtpEmailParticipants(
    val value: List<SmtpEmailParticipant>,
    val text: String,
)

data class SmtpEmailParticipant(
    val address: String,
    val name: String,
)

fun loadMultipartEmails(webClient: WebTestClient): List<MultipartEmail>? =
    webClient.get()
        .uri("http://${smtpContainer.host}:${smtpContainer.getMappedPort(1_080)}/api/emails")
        .exchange()
        .expectStatus()
        .isOk
        .expectBody<List<MultipartEmail>>()
        .returnResult()
        .responseBody

fun clearEmails(webClient: WebTestClient) {
    webClient.delete()
        .uri("http://${smtpContainer.host}:${smtpContainer.getMappedPort(1_080)}/api/emails")
        .exchange()
        .expectStatus()
        .isOk
}
