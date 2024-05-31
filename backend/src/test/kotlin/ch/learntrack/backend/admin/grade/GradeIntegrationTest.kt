package ch.learntrack.backend.admin.grade

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.admin.ADMIN_ROOT_URL
import ch.learntrack.backend.grade.CreateGradeDto
import ch.learntrack.backend.utils.createGradeFromTemplate
import ch.learntrack.backend.utils.createSchoolFromTemplate
import ch.learntrack.backend.utils.createAdminUserFromTemplate
import ch.learntrack.backend.utils.createUserSchoolFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.schoolTemplateId
import ch.learntrack.backend.utils.userAdminTemplateId
import org.springframework.http.HttpStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.BodyInserters
import java.util.UUID

private const val USER_NOT_ASSIGNED_TO_SCHOOL = "40d8b918-8f80-4b92-a3f5-4548d7883c54"

class GradeIntegrationTest: IntegrationTest() {

    private val userNotAssignedToSchoolId = UUID.fromString(USER_NOT_ASSIGNED_TO_SCHOOL)

    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            schoolDao.insert(createSchoolFromTemplate())
            gradeDao.insert(createGradeFromTemplate())
            userDao.insert(createAdminUserFromTemplate())
            userSchoolDao.insert(createUserSchoolFromTemplate(userAdminTemplateId))
            userDao.insert(createAdminUserFromTemplate
                (
                    id = userNotAssignedToSchoolId,
                    firstName = "test2",
                    lastName = "user2",
                    eMail = "test2user@gmail.com",
                ))
        }
    }

    @AfterEach
    fun cleanUp() {
        transactionManager.runInTransaction {
            gradeDao.deleteAll()
            schoolDao.deleteAll()
            userDao.deleteAll()
            userSchoolDao.deleteAll()
        }
    }

    @Test
    fun `should get all grades for assigned user`() {
        webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/grade")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun `should not get grades if user not assigned to school`() {
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

    @Test
    fun `should be able to create grade` () {
        val createGradeDto = CreateGradeDto(
            name = "Grade Test",
            schoolId = schoolTemplateId
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/grade/createGrade")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(createGradeDto))
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun `should not be able to create grade if it already exists in the same school` () {
        val createGradeDto = CreateGradeDto(
            name = "Class 1A",
            schoolId = schoolTemplateId
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/grade/createGrade")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .body(BodyInserters.fromValue(createGradeDto))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)

    }

    @Test
    fun `should not be able to create grade if admin not assigned to school` () {
        val createGradeDto = CreateGradeDto(
            name = "Grade Test",
            schoolId = schoolTemplateId
        )

        webClient.post()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/grade/createGrade")
                    .queryParam("schoolId", schoolTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userNotAssignedToSchoolId)) }
            .body(BodyInserters.fromValue(createGradeDto))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.FORBIDDEN)
    }
}