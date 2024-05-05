package ch.learntrack.backend

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "learntrack")
public data class BackendProperties @ConstructorBinding constructor(
    @NestedConfigurationProperty
    val jwt: JwtProperties,
    @NestedConfigurationProperty
    val backoffice: BackofficeProperties,
    @NestedConfigurationProperty
    val mailService: MailProperties,
)

public data class JwtProperties(
    val expirationInHours: Long,
    val key: String,
)

public data class BackofficeProperties(
    val username: String,
    val password: String,
)

public data class MailProperties(
    val senderMailAddress: String,
    val senderName: String,
)
