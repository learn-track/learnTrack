package ch.learntrack.backend.admin.teacher

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.admin.ADMIN_ROOT_URL
import ch.learntrack.backend.backoffice.user.UserDto
import ch.learntrack.backend.utils.createAdminUserFromTemplate
import ch.learntrack.backend.utils.createSchoolFromTemplate
import ch.learntrack.backend.utils.createTeacherUserFromTemplate
import ch.learntrack.backend.utils.createUserSchoolFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.schoolTemplateId
import ch.learntrack.backend.utils.userAdminTemplateId
import ch.learntrack.backend.utils.userTeacherTemplateId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

private const val USER_NOT_ASSIGNED_TO_SCHOOL = "40d8b918-8f50-4b32-a3f5-4548d7883c54"

class TeacherIntegrationTest : IntegrationTest() {

    private val userNotAssignedToSchoolId = UUID.fromString(USER_NOT_ASSIGNED_TO_SCHOOL)

    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            schoolDao.insert(createSchoolFromTemplate())
            userDao.insert(createAdminUserFromTemplate())
            userDao.insert(createTeacherUserFromTemplate())
            userDao.insert(createAdminUserFromTemplate
                (
                id = userNotAssignedToSchoolId,
                firstName = "testAdmin",
                lastName = "user2",
                eMail = "testAdminuser@gmail.com",
            ))
            userSchoolDao.insert(createUserSchoolFromTemplate(userAdminTemplateId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userTeacherTemplateId))
        }
    }


    @AfterEach
    fun cleanUp() {
        transactionManager.runInTransaction {
            schoolDao.deleteAll()
            userDao.deleteAll()
            userSchoolDao.deleteAll()
        }
    }

    @Test
    fun `should get all teachers for assigned school`() {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/teacher")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(UserDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response).hasSize(1)
        assertThat(response?.first()?.id).isEqualTo(userTeacherTemplateId)
    }

    @Test
    fun `should not get teachers if user not assigned to school`() {
        webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/teacher")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userNotAssignedToSchoolId)) }
            .exchange()
            .expectStatus()
            .isForbidden
    }
}