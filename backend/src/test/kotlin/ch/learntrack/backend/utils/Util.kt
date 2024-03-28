package ch.learntrack.backend.utils

import ch.learntrack.backend.BackendProperties
import org.springframework.test.web.reactive.server.WebTestClient

fun WebTestClient.RequestHeadersSpec<*>.setBasicAuthHeader(backendProperties: BackendProperties) =
    headers {
        it.setBearerAuth("no token")
        it.setBasicAuth(
            backendProperties.backoffice.username,
            backendProperties.backoffice.password,
        )
    }

fun WebTestClient.RequestHeadersSpec<*>.setBasicAuthHeader(username: String, password: String) =
    headers {
        it.setBearerAuth("no token")
        it.setBasicAuth(username, password)
    }
