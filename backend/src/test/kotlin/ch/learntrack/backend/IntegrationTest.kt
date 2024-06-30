package ch.learntrack.backend

import ch.learntrack.backend.persistence.tables.daos.*
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
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

val postgresSqlContainer = PostgreSQLContainer<Nothing>("postgres:16.2").apply {
    withDatabaseName("learntrack-backend")
    withUsername("backend")
    withPassword("backend")
}

val mockServerContainer = MockServerContainer(
    DockerImageName
        .parse("mockserver/mockserver")
        .withTag("mockserver-${MockServerClient::class.java.getPackage().implementationVersion}"),
)

val smtpContainer = GenericContainer<Nothing>("reachfive/fake-smtp-server:latest").apply {
    addExposedPorts(1_025, 1_080)
}

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(ac: GenericApplicationContext) {
        TestPropertyValues.of(
            "spring.datasource.url=${postgresSqlContainer.jdbcUrl}",
            "spring.datasource.username=${postgresSqlContainer.username}",
            "spring.datasource.password=${postgresSqlContainer.password}",
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
    lateinit var backendProperties: BackendProperties

    @Autowired
    lateinit var transactionManager: PlatformTransactionManager

    @Autowired
    lateinit var tokenService: ch.learntrack.backend.jwt.TokenService

    @Autowired
    lateinit var userDao: UserDao

    @Autowired
    lateinit var schoolDao: SchoolDao

    @Autowired
    lateinit var gradeDao: GradeDao

    @Autowired
    lateinit var subjectDao: SubjectDao

    @Autowired
    lateinit var userSchoolDao: UserSchoolDao

    @Autowired
    lateinit var userGradeDao: UserGradeDao

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
            Stream.of(postgresSqlContainer, mockServerContainer, smtpContainer).parallel()
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
    System.setProperty("MAIL_HOST", smtpContainer.host)
    System.setProperty("MAIL_PORT", smtpContainer.getMappedPort(1_025).toString())
}

