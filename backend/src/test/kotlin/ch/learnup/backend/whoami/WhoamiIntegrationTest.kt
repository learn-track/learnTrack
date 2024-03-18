package ch.learnup.backend.whoami

import ch.learnup.backend.IntegrationTest
import ch.learnup.backend.utils.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import java.util.*

private const val schoolNotAssigned = "eff58eb7-6214-468f-b7b4-2894d897dffb"
class WhoamiIntegrationTest: IntegrationTest() {

    private val schoolNotAssignedId = UUID.fromString(schoolNotAssigned)
    @BeforeEach
    fun seedDatabase() {
        transactionManager.runInTransaction {
            userDao.insert(createUserFromTemplate())
            schoolDao.insert(createSchoolFromTemplate())
            schoolDao.insert(createSchoolFromTemplate(id = schoolNotAssignedId))
            gradeDao.insert(createGradeFromTemplate())
            userSchoolDao.insert(createUserSchoolFromTemplate())
            userGradeDao.insert(createUserGradeFromTemplate())
        }
    }

    @AfterEach
    fun cleanUp() {
        transactionManager.runInTransaction {
            userDao.deleteAll()
            schoolDao.deleteAll()
            gradeDao.deleteAll()
            userSchoolDao.deleteAll()
            userGradeDao.deleteAll()
        }
    }

    @Test
    fun `should return Whoami dto`() {
        val result = webClient.get()
            .uri("/whoami")
            .exchange()
            .expectStatus().isOk()
            .expectBody(WhoamiDto::class.java)
            .returnResult().responseBody

        assertThat(requireNotNull(result?.user?.id))
            .isEqualTo(userTemplateId)

        assertThat(requireNotNull(result?.school?.any { it.id == schoolTemplateId }))
            .isEqualTo(true)

        assertThat(requireNotNull(result?.grade?.any { it.id == gradeTemplateId }))
            .isEqualTo(true)
    }

    @Test
    fun `should not return School for User`() {
        val result = webClient.get()
            .uri("/whoami")
            .exchange()
            .expectStatus().isOk()
            .expectBody(WhoamiDto::class.java)
            .returnResult().responseBody

        assertThat(requireNotNull(result?.user?.id))
            .isEqualTo(userTemplateId)

        assertThat(requireNotNull(result?.school?.any { it.id == schoolNotAssignedId }))
            .isEqualTo(false)

        assertThat(requireNotNull(result?.grade?.any { it.id == gradeTemplateId }))
            .isEqualTo(true)
    }
}