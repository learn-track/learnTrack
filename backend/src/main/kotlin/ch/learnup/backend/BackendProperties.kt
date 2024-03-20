package ch.learnup.backend

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "learnup")
public data class BackendProperties @ConstructorBinding constructor(
    @NestedConfigurationProperty
    val jwt: JwtProperties,
)

public data class JwtProperties(
    val expirationInHours: Long,
    val key: String,
)
