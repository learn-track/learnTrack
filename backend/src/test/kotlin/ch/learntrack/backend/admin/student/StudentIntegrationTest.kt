package ch.learntrack.backend.admin.student

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.admin.ADMIN_ROOT_URL
import ch.learntrack.backend.utils.createAdminUserFromTemplate
import ch.learntrack.backend.utils.createGradeFromTemplate
import ch.learntrack.backend.utils.createSchoolFromTemplate
import ch.learntrack.backend.utils.createStudentUserFromTemplate
import ch.learntrack.backend.utils.createUserGradeFromTemplate
import ch.learntrack.backend.utils.createUserSchoolFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.gradeTemplateId
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.schoolTemplateId
import ch.learntrack.backend.utils.userAdminTemplateId
import ch.learntrack.backend.utils.userStudentTemplateId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

private const val USER_WITH_STUDENT_ROLE = "40d8b918-8f50-4b32-a3f5-4548d7883c55"
private const val USER_NOT_ASSIGNED_TO_SCHOOL = "40d8b918-8f50-4b32-a3f5-4548d7883c54"

class StudentIntegrationTest : IntegrationTest() {

    private val userIdWithStudentRole = UUID.fromString(USER_WITH_STUDENT_ROLE)
    private val userNotAssignedToSchoolId = UUID.fromString(USER_NOT_ASSIGNED_TO_SCHOOL)

    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            schoolDao.insert(createSchoolFromTemplate())
            gradeDao.insert(createGradeFromTemplate())
            userDao.insert(createAdminUserFromTemplate())
            userDao.insert(createStudentUserFromTemplate())
            userDao.insert(createStudentUserFromTemplate
                (
                id = userIdWithStudentRole,
                firstName = "studentUser",
                lastName = "test",
                eMail = "studentUser.test@gmail.com"
            ))
            userDao.insert(createAdminUserFromTemplate
                (
                id = userNotAssignedToSchoolId,
                firstName = "testAdmin",
                lastName = "user2",
                eMail = "testAdminuser@gmail.com",
            ))
            userSchoolDao.insert(createUserSchoolFromTemplate(userAdminTemplateId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userStudentTemplateId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userIdWithStudentRole))
            userGradeDao.insert(createUserGradeFromTemplate(userStudentTemplateId))
            userGradeDao.insert(createUserGradeFromTemplate(userIdWithStudentRole))
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
    fun `should get StudentDetailsDto for assigned school`() {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/student")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(StudentDetailsDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response).hasSize(2)
        assertThat(response)
            .anyMatch { it.grade.id == gradeTemplateId }
            .noneMatch { it.user.id == userNotAssignedToSchoolId }
    }

    @Test
    fun `should not get StudentDetailsDto if user not assigned to school`() {
        webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/grade")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userNotAssignedToSchoolId)) }
            .exchange()
            .expectStatus()
            .isForbidden
    }
}