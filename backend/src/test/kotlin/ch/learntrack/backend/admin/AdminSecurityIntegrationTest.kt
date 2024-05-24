package ch.learntrack.backend.admin

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.admin.grade.CreateGradeDto
import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.utils.createSchoolFromTemplate
import ch.learntrack.backend.utils.createUserFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.schoolTemplateId
import ch.learntrack.backend.utils.setBasicAuthHeader
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.BodyInserters
import java.util.UUID

private const val TEACHER_UUID = "40d8b918-8f80-4b92-a3f5-4548d7883c54"
private const val STUDENT_UUID = "40d8b918-8f80-4b92-a3f5-4548d7883c55"

class AdminSecurityIntegrationTest: IntegrationTest() {
    private val teacherUUID = UUID.fromString(TEACHER_UUID)
    private val studentUUID = UUID.fromString(STUDENT_UUID)

    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            schoolDao.insert(createSchoolFromTemplate())
            userDao.insert(createUserFromTemplate
                (
                id = teacherUUID,
                firstName = "teacher",
                lastName = "user2",
                eMail = "teacher@gmail.com",
                password = "test23d!daT",
                userRole = UserRole.TEACHER
            ))
            userDao.insert(createUserFromTemplate
                (
                id = studentUUID,
                firstName = "student",
                lastName = "test",
                eMail = "student@gmail.com",
                password = "test23d!daT",
                userRole = UserRole.STUDENT
            ))
        }
    }

    @AfterEach
    fun cleanUp() {
        transactionManager.runInTransaction {
            schoolDao.deleteAll()
            userDao.deleteAll()
        }
    }

    @Test
    fun `disallow access to admin endpoints for users with wrong credentials`() {
        webClient.post()
            .uri(ADMIN_ROOT_URL)
            .setBasicAuthHeader("wrong", "credentials")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `disallow access to admin endpoints for teachers`() {
        val createGradeDto = CreateGradeDto(
            name = "Grade Test",
            schoolId = schoolTemplateId
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path(ADMIN_ROOT_URL)
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(teacherUUID)) }
            .body(BodyInserters.fromValue(createGradeDto))
            .exchange()
            .expectStatus()
            .isForbidden
    }

    @Test
    fun `disallow access to admin endpoints for students`() {
        val createGradeDto = CreateGradeDto(
            name = "Grade Test",
            schoolId = schoolTemplateId
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path(ADMIN_ROOT_URL)
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(studentUUID)) }
            .body(BodyInserters.fromValue(createGradeDto))
            .exchange()
            .expectStatus()
            .isForbidden
    }
}