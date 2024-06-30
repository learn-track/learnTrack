package ch.learntrack.backend.admin

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.admin.grade.CreateGradeDto
import ch.learntrack.backend.utils.createSchoolFromTemplate
import ch.learntrack.backend.utils.createStudentUserFromTemplate
import ch.learntrack.backend.utils.createTeacherUserFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.schoolTemplateId
import ch.learntrack.backend.utils.setBasicAuthHeader
import ch.learntrack.backend.utils.userStudentTemplateId
import ch.learntrack.backend.utils.userTeacherTemplateId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.BodyInserters

class AdminSecurityIntegrationTest: IntegrationTest() {
    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            schoolDao.insert(createSchoolFromTemplate())
            userDao.insert(createTeacherUserFromTemplate(subjectId = null))
            userDao.insert(createStudentUserFromTemplate())
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
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userTeacherTemplateId)) }
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
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userStudentTemplateId)) }
            .body(BodyInserters.fromValue(createGradeDto))
            .exchange()
            .expectStatus()
            .isForbidden

    }
}