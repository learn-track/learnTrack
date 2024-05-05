package ch.learntrack.backend.email

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.clearEmails
import ch.learntrack.backend.loadMultipartEmails
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class MailSenderTest: IntegrationTest() {
    @Autowired
    lateinit var mailSender: MailSender

    @AfterEach
    fun cleanUp() {
        clearEmails(webClient)
    }

    @Test
    fun sendMail() {
        mailSender.sendMail("receiver@learnTrack.ch", "Test Mail Subject", "Test Mail Content")

        val emails = loadMultipartEmails(webClient)

        assertThat(emails).hasSize(1)
        emails?.firstOrNull()?.let { email ->
            assertThat(email.to.value).hasSize(1)
            email.to.value.firstOrNull()?.let { receiver ->
                assertThat(receiver.address).isEqualTo("receiver@learnTrack.ch")
            }
            assertThat(email.from.value).hasSize(1)
            email.from.value.firstOrNull()?.let { sender ->
                assertThat(sender.address).isEqualTo("support-test@learntrack.ch")
            }
            assertThat(email.subject).isEqualTo("Test Mail Subject")
            assertThat(email.html).contains("Test Mail Content")
        }
    }
}
