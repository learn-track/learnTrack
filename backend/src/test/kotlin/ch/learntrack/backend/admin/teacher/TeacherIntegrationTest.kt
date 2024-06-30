package ch.learntrack.backend.admin.teacher

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.admin.ADMIN_ROOT_URL
import ch.learntrack.backend.backoffice.user.UserDto
import ch.learntrack.backend.utils.createAdminUserFromTemplate
import ch.learntrack.backend.utils.createSchoolFromTemplate
import ch.learntrack.backend.utils.createStudentUserFromTemplate
import ch.learntrack.backend.utils.createTeacherUserFromTemplate
import ch.learntrack.backend.utils.createUserSchoolFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.schoolTemplateId
import ch.learntrack.backend.utils.userAdminTemplateId
import ch.learntrack.backend.utils.userStudentTemplateId
import ch.learntrack.backend.utils.userTeacherTemplateId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.util.UUID

private const val ADMIN_USER_NOT_ASSIGNED_TO_SCHOOL = "40d8b918-8f50-4b32-a3f5-4548d7883c54"
private const val TEACHER_USER_NOT_ASSIGNED_TO_SCHOOL = "40d8b918-8f50-4b32-a3f5-4548d7883c55"

class TeacherIntegrationTest : IntegrationTest() {

    private val adminUserNotAssignedToSchoolId = UUID.fromString(ADMIN_USER_NOT_ASSIGNED_TO_SCHOOL)
    private val teacherUserNotAssignedToSchoolId = UUID.fromString(TEACHER_USER_NOT_ASSIGNED_TO_SCHOOL)

    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            schoolDao.insert(createSchoolFromTemplate())
            userDao.insert(createAdminUserFromTemplate())
            userDao.insert(createTeacherUserFromTemplate(subjectId = null))
            userDao.insert(createStudentUserFromTemplate())
            userDao.insert(createAdminUserFromTemplate
                (
                id = adminUserNotAssignedToSchoolId,
                firstName = "testAdmin",
                lastName = "user2",
                eMail = "testAdminuser@gmail.com",
            ))
            userDao.insert(createTeacherUserFromTemplate(
                id = teacherUserNotAssignedToSchoolId,
                firstName = "testTeacher",
                lastName = "userTeacher2",
                eMail = "testTeacheruser@gmail.com",
                subjectId = null,
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
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(adminUserNotAssignedToSchoolId)) }
            .exchange()
            .expectStatus()
            .isForbidden
    }

    @Test
    fun `should get Teachers with Email characters`() {
        val response1 = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/teacher/search")
                    .queryParam("schoolId", schoolTemplateId)
                    .queryParam("email", "tea")
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(TeacherDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response1).isNotNull
        assertThat(response1?.first()?.id).isEqualTo(userTeacherTemplateId)
        assertThat(response1?.first()?.email).isEqualTo("teacheruser@gmail.com")
        assertThat(response1?.first()?.isAssignedToSchool).isTrue()

        val response2 = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/teacher/search")
                    .queryParam("schoolId", schoolTemplateId)
                    .queryParam("email", "tes")
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(TeacherDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response2).isNotNull
        assertThat(response2?.first()?.id).isEqualTo(teacherUserNotAssignedToSchoolId)
        assertThat(response2?.first()?.email).isEqualTo("testTeacheruser@gmail.com")
        assertThat(response2?.first()?.isAssignedToSchool).isFalse()
    }

    @Test
    fun `should not get teachers if email characters are less than three characters long`() {
        val response =  webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/teacher/search")
                    .queryParam("schoolId", schoolTemplateId)
                    .queryParam("email", "te")
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(TeacherDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isEmpty()
    }

    @Test
    fun `should assigned teacher to school`() {
        webClient.put()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/teacher/assignTeacherToSchool")
                    .queryParam("schoolId", schoolTemplateId)
                    .queryParam("teacherId", teacherUserNotAssignedToSchoolId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .exchange()
            .expectStatus()
            .isOk

        assertThat(userSchoolDao.fetchBySchoolId(schoolTemplateId).any { it.userId == teacherUserNotAssignedToSchoolId}).isTrue()
    }

    @Test
    fun `should not assigned teacher to school if role is not teacher`() {
        webClient.put()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/teacher/assignTeacherToSchool")
                    .queryParam("schoolId", schoolTemplateId)
                    .queryParam("teacherId", userStudentTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)
    }

    @Test
    fun `should not assigned teacher to school if teacher is already assigned`() {
        webClient.put()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$ADMIN_ROOT_URL/teacher/assignTeacherToSchool")
                    .queryParam("schoolId", schoolTemplateId)
                    .queryParam("teacherId", userTeacherTemplateId)
                    .build()
            }
            .headers { headers -> headers.setBearerAuth(tokenService.createJwtToken(userAdminTemplateId)) }
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)
    }
}