package ch.v7.backend

import ch.learnup.backend.BackendApplication
import ch.learnup.backend.beans
import java.util.stream.Stream
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.mockserver.client.MockServerClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.transaction.PlatformTransactionManager
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.utility.DockerImageName

val mariaDbContainer = MariaDBContainer<Nothing>("mariadb:11.2.2").apply {
    withDatabaseName("v7_backend")
    withUsername("backend")
    withPassword("backend")
}

val mockServerContainer = MockServerContainer(
    DockerImageName
        .parse("mockserver/mockserver")
        .withTag("mockserver-${MockServerClient::class.java.getPackage().implementationVersion}"),
)

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(ac: GenericApplicationContext) {
        TestPropertyValues.of(
            "spring.datasource.url=${mariaDbContainer.jdbcUrl}",
            "spring.datasource.username=${mariaDbContainer.username}",
            "spring.datasource.password=${mariaDbContainer.password}",
        ).applyTo(ac.environment)
        beans.forEach { it.initialize(ac) }
    }
}

@ActiveProfiles("test")
@ContextConfiguration(initializers = [BeansInitializer::class], classes = [BackendApplication::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTest {
    @LocalServerPort
    lateinit var localServerPort: String
    lateinit var webClient: WebTestClient

    @Autowired
    lateinit var transactionManager: PlatformTransactionManager

    @BeforeEach
    fun beforeEach() {
        webClient =
            WebTestClient.bindToServer()
                .baseUrl("http://localhost:$localServerPort")
                .build()
    }

    companion object {
        lateinit var mockServerClient: MockServerClient

        @BeforeAll
        @JvmStatic
        fun beforeAllStatic() {
            Stream.of(mariaDbContainer, mockServerContainer).parallel()
                .forEach(GenericContainer<*>::start)
            setSystemProperties()
            mockServerClient = MockServerClient(mockServerContainer.host, mockServerContainer.serverPort)
        }

        @AfterEach
        fun reset() {
            MockServerClient(mockServerContainer.host, mockServerContainer.serverPort).reset()
        }
    }
}

private fun setSystemProperties() {
    System.setProperty("MOCK_WEB_SERVER", mockServerContainer.endpoint)
}

