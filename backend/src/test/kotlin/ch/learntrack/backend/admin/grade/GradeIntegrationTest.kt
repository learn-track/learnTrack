package ch.learntrack.backend.admin.grade

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.admin.ADMIN_ROOT_URL
import ch.learntrack.backend.utils.createGradeFromTemplate
import ch.learntrack.backend.utils.createSchoolFromTemplate
import ch.learntrack.backend.utils.createAdminUserFromTemplate
import ch.learntrack.backend.utils.createStudentUserFromTemplate
import ch.learntrack.backend.utils.createSubjectFromTemplate
import ch.learntrack.backend.utils.createTeacherUserFromTemplate
import ch.learntrack.backend.utils.createUserGradeFromTemplate
import ch.learntrack.backend.utils.createUserSchoolFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.gradeTemplateId
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.schoolTemplateId
import ch.learntrack.backend.utils.subjectTemplateId
import ch.learntrack.backend.utils.userAdminTemplateId
import ch.learntrack.backend.utils.userStudentTemplateId
import ch.learntrack.backend.utils.userTeacherTemplateId
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.BodyInserters
import java.util.UUID

private const val ETH_SCHOOL = "40d8b918-8f80-4b92-a3f5-4548d7883c59"
private const val ETH_GRADE = "40d8b918-8f80-4b92-a3f5-4548d7883c60"
private const val ETH_SUBJECT = "40d8b918-8f80-4b92-a3f5-4548d7883c61"
private const val USER_ASSIGNED_TO_DIFFERENT_SCHOOL = "40d8b918-8f80-4b92-a3f5-4548d7883c58"

class GradeIntegrationTest: IntegrationTest() {

    private val ethSchoolId = UUID.fromString(ETH_SCHOOL)
    private val ethGradeId = UUID.fromString(ETH_GRADE)
    private val ethSubjectId = UUID.fromString(ETH_SUBJECT)
    private val userAssignedToDifferentSchoolId = UUID.fromString(USER_ASSIGNED_TO_DIFFERENT_SCHOOL)

    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            // Schools
            schoolDao.insert(createSchoolFromTemplate())
            schoolDao.insert(createSchoolFromTemplate(id = ethSchoolId, name = "ETH"))
            // Grades
            gradeDao.insert(createGradeFromTemplate())
            gradeDao.insert(createGradeFromTemplate(id = ethGradeId, name = "Grade 1A", schoolId = ethSchoolId))
            // Users
            userDao.insert(createAdminUserFromTemplate())
            userDao.insert(createTeacherUserFromTemplate())
            userDao.insert(createStudentUserFromTemplate())
            userDao.insert(createAdminUserFromTemplate
                (
                id = userAssignedToDifferentSchoolId,
                firstName = "test2",
                lastName = "user2",
                eMail = "test2user@gmail.com",
            ))
            // Subjects
            subjectDao.insert(createSubjectFromTemplate())
            subjectDao.insert(createSubjectFromTemplate(id = ethSubjectId, name = "Quantum Physics", gradeId = ethGradeId))
            // User and school assignments
            userSchoolDao.insert(createUserSchoolFromTemplate(userAdminTemplateId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userTeacherTemplateId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userStudentTemplateId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userId = userAssignedToDifferentSchoolId, schoolId = ethSchoolId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userId = userTeacherTemplateId, schoolId = ethSchoolId))
            // User and grade assignments
            userGradeDao.insert(createUserGradeFromTemplate(userTeacherTemplateId))
            userGradeDao.insert(createUserGradeFromTemplate(userStudentTemplateId))
            userGradeDao.insert(createUserGradeFromTemplate(userId = userTeacherTemplateId, gradeId = ethGradeId)
            )
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
    fun `should get GradeInfoDto for assigned user`() {
        val response = webClient.get()
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
            .expectBodyList(GradeInfoDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response).hasSize(1)
    }

    @Test
    fun `should get all grades for assigned user`() {
        val response = webClient.get()
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
            .expectBodyList(GradeInfoDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response).hasSize(1)
        assertThat(response?.first()?.grades?.id).isNotEqualTo(ethGradeId)
        assertThat(response?.first()?.grades?.id).isEqualTo(gradeTemplateId)
    }

    @Test
    fun `should get all users for assigned user`() {
        val response = webClient.get()
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
            .expectBodyList(GradeInfoDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response).hasSize(1)
        assertThat(response?.first()?.teachers?.first()?.id).isEqualTo(userTeacherTemplateId)
        assertThat(response?.first()?.students?.first()?.id).isEqualTo(userStudentTemplateId)
    }

    @Test
    fun `should get same teacher for assigned users in different schools`() {
        val response1 = webClient.get()
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
            .expectBodyList(GradeInfoDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response1).isNotNull
        assertThat(response1).hasSize(1)
        assertThat(response1?.first()?.teachers?.first()?.id).isEqualTo(userTeacherTemplateId)

        val response2 = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/grade")
                    .queryParam("schoolId", ethSchoolId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAssignedToDifferentSchoolId)) }
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(GradeInfoDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response2).isNotNull
        assertThat(response2).hasSize(1)
        assertThat(response2?.first()?.teachers?.first()?.id).isEqualTo(userTeacherTemplateId)
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
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAssignedToDifferentSchoolId)) }
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
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAssignedToDifferentSchoolId)) }
            .body(BodyInserters.fromValue(createGradeDto))
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun `should get GradeDetailsDto for assigned user` () {
        val response =  webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/grade/getGradeDetails")
                    .queryParam("schoolId", schoolTemplateId)
                    .queryParam("gradeId", gradeTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(GradeDetailsDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response).hasSize(1)
        assertThat(response?.map { it.students.any { it.id == userStudentTemplateId } })
        assertThat(response?.map { it.subjectDetailsDto.any { it.teacher?.id == userTeacherTemplateId } })
        assertThat(response?.map { it.subjectDetailsDto.any { it.id == subjectTemplateId } })

        val response1 =  webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/grade/getGradeDetails")
                    .queryParam("schoolId", ethSchoolId)
                    .queryParam("gradeId", ethGradeId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAssignedToDifferentSchoolId)) }
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(GradeDetailsDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response1).isNotNull
        assertThat(response1?.map { it.students.isEmpty()})
        assertThat(response1?.map { it.subjectDetailsDto.any { it.teacher == null } })
        assertThat(response1?.map { it.subjectDetailsDto.any { it.id == ethSubjectId } })
    }
}