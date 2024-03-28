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
)

public data class JwtProperties(
    val expirationInHours: Long,
    val key: String,
)

public data class BackofficeProperties(
    val username: String,
    val password: String,
)
