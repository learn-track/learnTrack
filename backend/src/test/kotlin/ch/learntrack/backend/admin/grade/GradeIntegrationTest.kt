package ch.learntrack.backend.admin.grade

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.utils.createGradeFromTemplate
import ch.learntrack.backend.utils.createSchoolFromTemplate
import ch.learntrack.backend.utils.createUserFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.schoolTemplateId
import org.springframework.http.HttpStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.BodyInserters


class GradeIntegrationTest: IntegrationTest() {

    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            schoolDao.insert(createSchoolFromTemplate())
            gradeDao.insert(createGradeFromTemplate())
            userDao.insert(createUserFromTemplate())
        }
    }

    @AfterEach
    fun cleanUp() {
        transactionManager.runInTransaction {
            gradeDao.deleteAll()
            schoolDao.deleteAll()
            userDao.deleteAll()
        }
    }

    @Test
    fun `should be able to create grade` () {
        val createGradeDto = CreateGradeDto(
            name = "Grade Test",
            schoolId = schoolTemplateId
        )

        webClient.post()
            .uri("/admin/createGrade")
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
            .uri("/admin/createGrade")
            .body(BodyInserters.fromValue(createGradeDto))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)

    }
}