package ch.learntrack.backend.backoffice.user

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.utils.createUserFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.setBasicAuthHeader
import ch.learntrack.backend.utils.userTemplateId
import java.util.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val TEACHER_USER = "5003e059-9cc0-4e86-a89d-9d64db1c1f1f"

class UserIntegrationTest: IntegrationTest() {

    private val teacherUserId = UUID.fromString(TEACHER_USER)

    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            userDao.insert(createUserFromTemplate())
            userDao.insert(createUserFromTemplate(id = teacherUserId, eMail = "teacher@gmail.com", userRole = UserRole.TEACHER))
        }
    }

    @AfterEach
    fun cleanUp() {
        transactionManager.runInTransaction {
            userDao.deleteAll()
        }
    }

    @Test
    fun `should get 401 with wrong credentials`() {
        webClient.get()
            .uri("/backoffice/user")
            .setBasicAuthHeader("wrong", "credentials")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `should return a list with only admin users`() {
        val response = webClient.get()
            .uri("/backoffice/user")
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectBodyList(UserDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response).hasSize(1)
        assertThat(response?.first()?.id).isEqualTo(userTemplateId)
    }
}
