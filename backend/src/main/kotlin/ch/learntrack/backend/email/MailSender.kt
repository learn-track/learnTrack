package ch.learntrack.backend.email

import ch.learntrack.backend.BackendProperties
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper

public class MailSender(private val mailSender: JavaMailSender, backendProperties: BackendProperties) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val senderAddress = backendProperties.mailService.senderMailAddress
    private val senderName = backendProperties.mailService.senderName

    public fun sendMail(
        receiverAddress: String,
        mailSubject: String,
        mailContent: String,
    ) {
        log.info("Send email to '{}' with subject '{}'", receiverAddress, mailSubject)
        try {
            val message = mailSender.createMimeMessage()
            MimeMessageHelper(message, true).apply {
                setFrom("$senderName <$senderAddress>")
                setTo(receiverAddress)
                setSubject(mailSubject)
                setText(mailContent, true)
            }
            mailSender.send(message)
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            log.error(
                "Could not deliver email to '{}' with subject '{}': {}",
                receiverAddress,
                mailSubject,
                e.message,
                e,
            )
            throw e
        }
    }
}
