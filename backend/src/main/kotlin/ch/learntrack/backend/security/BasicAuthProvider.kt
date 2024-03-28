package ch.learntrack.backend.security

import ch.learntrack.backend.BackendProperties
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.provisioning.InMemoryUserDetailsManager

public const val SECURITY_ROLE_BACKOFFICE: String = "BACKOFFICE"

public class BasicAuthProvider(backendProperties: BackendProperties) : DaoAuthenticationProvider() {
    init {
        val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        userDetailsService = InMemoryUserDetailsManager(
            User
                .withUsername(backendProperties.backoffice.username)
                .password(backendProperties.backoffice.password)
                .passwordEncoder(encoder::encode)
                .roles(SECURITY_ROLE_BACKOFFICE)
                .build(),
        )
    }
}
