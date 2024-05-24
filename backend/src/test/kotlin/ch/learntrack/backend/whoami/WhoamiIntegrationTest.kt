package ch.learntrack.backend.whoami

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.utils.createGradeFromTemplate
import ch.learntrack.backend.utils.createSchoolFromTemplate
import ch.learntrack.backend.utils.createAdminUserFromTemplate
import ch.learntrack.backend.utils.createUserGradeFromTemplate
import ch.learntrack.backend.utils.createUserSchoolFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.gradeTemplateId
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.schoolTemplateId
import ch.learntrack.backend.utils.userAdminTemplateId
import java.util.UUID
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

private const val SCHOOL_NOT_ASSIGNED = "eff58eb7-6214-468f-b7b4-2894d897dffb"

class WhoamiIntegrationTest: IntegrationTest() {

    private val schoolNotAssignedId = UUID.fromString(SCHOOL_NOT_ASSIGNED)
    @BeforeEach
    fun seedDatabase() {
        transactionManager.runInTransaction {
            userDao.insert(createAdminUserFromTemplate())
            schoolDao.insert(createSchoolFromTemplate())
            schoolDao.insert(createSchoolFromTemplate(id = schoolNotAssignedId))
            gradeDao.insert(createGradeFromTemplate())
            userSchoolDao.insert(createUserSchoolFromTemplate())
            userGradeDao.insert(createUserGradeFromTemplate(userId = userAdminTemplateId))
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
    fun `should return whoami dto`() {
        val result = webClient.get()
            .uri("/whoami")
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(WhoamiDto::class.java)
            .returnResult()
            .responseBody

        assertThat(requireNotNull(result?.user?.id))
            .isEqualTo(userAdminTemplateId)

        assertThat(requireNotNull(result?.schools?.any { it.id == schoolTemplateId }))
            .isEqualTo(true)

        assertThat(requireNotNull(result?.grades?.any { it.id == gradeTemplateId }))
            .isEqualTo(true)
    }

    @Test
    fun `should not return school for User`() {
        val result = webClient.get()
            .uri("/whoami")
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(WhoamiDto::class.java)
            .returnResult()
            .responseBody

        assertThat(requireNotNull(result?.user?.id))
            .isEqualTo(userAdminTemplateId)

        assertThat(requireNotNull(result?.schools?.any { it.id == schoolNotAssignedId }))
            .isEqualTo(false)

        assertThat(requireNotNull(result?.grades?.any { it.id == gradeTemplateId }))
            .isEqualTo(true)
    }
}