package ch.learntrack.backend.backoffice.school

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.backoffice.BACKOFFICE_ROOT_URL
import ch.learntrack.backend.backoffice.user.UserDto
import ch.learntrack.backend.persistence.tables.references.SCHOOL
import ch.learntrack.backend.utils.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.BodyInserters
import java.util.UUID

private const val ETH_SCHOOL = "45ffeb7e-ce1c-46a2-8415-fe2b4196c12c"
private const val BENEDICT_SCHOOL = "baaf056a-fa82-4ce6-a89c-92a7416f1db8"
private const val PHZ_SCHOOL = "65b454d0-f496-4df1-ae11-01a87b618a6e"
private const val ADMIN_USER_SECOND_SCHOOL = "90f8dffc-3bd8-4ff4-b2c9-71b48aa57ede"
private const val STUDENT_ONE = "38b75a0a-be31-4094-a450-0911570afb01"
private const val STUDENT_TWO = "7e02c5c0-5a8d-4e62-8842-0f13af9d89e3"
private const val STUDENT_THREE = "0e126135-1a3f-431a-b9d2-b302834ca1c8"
private const val TEACHER_ONE = "0b4bade8-1ffd-47f2-be90-568117f49af9"
private const val TEACHER_TWO = "010103b4-2709-46d8-b263-93cdf1669aab"
private const val TEACHER_THREE = "c7357eae-af74-4fb1-aad5-48a95da23370"

class SchoolIntegrationTest: IntegrationTest() {
    private val ethSchoolId = UUID.fromString(ETH_SCHOOL)
    private val benedictSchoolId = UUID.fromString(BENEDICT_SCHOOL)
    private val phzSchoolId = UUID.fromString(PHZ_SCHOOL)
    private val adminUserSecondSchoolId = UUID.fromString(ADMIN_USER_SECOND_SCHOOL)
    private val studentOneId = UUID.fromString(STUDENT_ONE)
    private val studentTwoId = UUID.fromString(STUDENT_TWO)
    private val studentThreeId = UUID.fromString(STUDENT_THREE)
    private val teacherOneId = UUID.fromString(TEACHER_ONE)
    private val teacherTwoId = UUID.fromString(TEACHER_TWO)
    private val teacherThreeId = UUID.fromString(TEACHER_THREE)

    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            schoolDao.insert(createSchoolFromTemplate())
            schoolDao.insert(createSchoolFromTemplate(id = ethSchoolId, name = "ETH"))
            schoolDao.insert(createSchoolFromTemplate(id = benedictSchoolId, name = "BENEDICT"))
            schoolDao.insert(createSchoolFromTemplate(id = phzSchoolId, name = "PHZ"))
            userDao.insert(createAdminUserFromTemplate())
            userDao.insert(createAdminUserFromTemplate(id = adminUserSecondSchoolId, eMail = ADMIN_USER_SECOND_SCHOOL))
            userDao.insert(createStudentUserFromTemplate(id = studentOneId, eMail = STUDENT_ONE))
            userDao.insert(createStudentUserFromTemplate(id = studentTwoId, eMail = STUDENT_TWO))
            userDao.insert(createStudentUserFromTemplate(id = studentThreeId, eMail = STUDENT_THREE))
            userDao.insert(createTeacherUserFromTemplate(id = teacherOneId, eMail = TEACHER_ONE))
            userDao.insert(createTeacherUserFromTemplate(id = teacherTwoId, eMail = TEACHER_TWO))
            userDao.insert(createTeacherUserFromTemplate(id = teacherThreeId, eMail = TEACHER_THREE))
            userSchoolDao.insert(createUserSchoolFromTemplate(userId = userAdminTemplateId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userId = userAdminTemplateId, schoolId = ethSchoolId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userId = adminUserSecondSchoolId, schoolId = phzSchoolId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userId = studentOneId, schoolId = phzSchoolId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userId = studentTwoId, schoolId = phzSchoolId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userId = studentThreeId, schoolId = phzSchoolId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userId = teacherOneId, schoolId = phzSchoolId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userId = teacherTwoId, schoolId = phzSchoolId))
            userSchoolDao.insert(createUserSchoolFromTemplate(userId = teacherThreeId, schoolId = phzSchoolId))
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
    fun `should get 401 with wrong credentials`() {
        webClient.get()
            .uri("$BACKOFFICE_ROOT_URL/school")
            .setBasicAuthHeader("wrong", "credentials")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `should return a list with schools`() {
        val response = webClient.get()
            .uri("$BACKOFFICE_ROOT_URL/school")
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectBodyList(SchoolDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response?.first()?.id).isEqualTo(schoolTemplateId)
    }

    @Test
    fun `should be able to create a school`() {
        val schoolDto = CreateSchoolDto("TEST_SCHOOL", "Teststrasse 214", "Zürich", 8405)

        webClient.post()
            .uri("$BACKOFFICE_ROOT_URL/school/create")
            .body(BodyInserters.fromValue(schoolDto))
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isOk

        val school = schoolDao.fetchOne(SCHOOL.NAME, schoolDto.name)

        assertThat(school).isNotNull
        assertThat(school?.name).isEqualTo(schoolDto.name)
        assertThat(school?.address).isEqualTo(schoolDto.address)
        assertThat(school?.city).isEqualTo(schoolDto.city)
        assertThat(school?.postcode).isEqualTo(schoolDto.postcode)
    }

    @Test
    fun `should throw exception if all school parameters are the same`() {
        val schoolDtoExisting = CreateSchoolDto("Benedict", "Vulkanstrasse 106", "Zürich", 8048)

        webClient.post()
            .uri("$BACKOFFICE_ROOT_URL/school/create")
            .body(BodyInserters.fromValue(schoolDtoExisting))
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)

        val school = schoolDao.fetchOne(SCHOOL.NAME, schoolDtoExisting.name)

        assertThat(school?.name).isEqualTo(schoolDtoExisting.name)
        assertThat(school?.address).isEqualTo(schoolDtoExisting.address)
        assertThat(school?.city).isEqualTo(schoolDtoExisting.city)
        assertThat(school?.postcode).isEqualTo(schoolDtoExisting.postcode)
    }

    @Test
    fun `should throw exception if certain school parameters are the same`() {
        val schoolDtoExisting = CreateSchoolDto("ETH", "Vulkanstrasse 112", "Zürich", 8048)

        webClient.post()
            .uri("$BACKOFFICE_ROOT_URL/school/create")
            .body(BodyInserters.fromValue(schoolDtoExisting))
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isEqualTo(HttpStatus.CONFLICT)

        val school = schoolDao.fetchOne(SCHOOL.NAME, schoolDtoExisting.name)

        assertThat(school?.name).isEqualTo(schoolDtoExisting.name)
        assertThat(school?.address).isNotEqualTo(schoolDtoExisting.address)
        assertThat(school?.city).isEqualTo(schoolDtoExisting.city)
        assertThat(school?.postcode).isEqualTo(schoolDtoExisting.postcode)
    }

    @Test
    fun `should return all schools for admin user`() {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$BACKOFFICE_ROOT_URL/school")
                    .queryParam("assignedUserId", userAdminTemplateId)
                    .build()
            }
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectBodyList(SchoolDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response?.first()?.id).isEqualTo(schoolTemplateId)

        val response2 = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$BACKOFFICE_ROOT_URL/school")
                    .queryParam("assignedUserId", userAdminTemplateId)
                    .build()
            }
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectBodyList(SchoolDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response2).isNotNull
        assertThat(response2).hasSize(2)
        assertThat(response2?.any { it.id ==  schoolTemplateId}).isTrue()
        assertThat(response2?.any { it.id ==  ethSchoolId}).isTrue()
        assertThat(response2?.any { it.id !=  phzSchoolId}).isTrue()

        val response3 = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$BACKOFFICE_ROOT_URL/school")
                    .queryParam("assignedUserId", adminUserSecondSchoolId)
                    .build()
            }
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectBodyList(SchoolDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response3).isNotNull
        assertThat(response3?.any { it.id !=  schoolTemplateId}).isTrue()
        assertThat(response3?.any { it.id !=  ethSchoolId}).isTrue()
        assertThat(response3?.any { it.id ==  phzSchoolId}).isTrue()
    }

    @Test
    fun `should return correct teacher count for school`() {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$BACKOFFICE_ROOT_URL/user/getTeacherCount")
                    .queryParam("schoolId", phzSchoolId)
                    .build()
            }
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Int::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response).isEqualTo(3)
    }

    @Test
    fun `should return correct student count for school`() {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$BACKOFFICE_ROOT_URL/user/getStudentCount")
                    .queryParam("schoolId", phzSchoolId)
                    .build()
            }
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Int::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response).isEqualTo(3)
    }

    @Test
    fun `should return all admins for school`() {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("$BACKOFFICE_ROOT_URL/user/getAllAdminsBySchoolId")
                    .queryParam("schoolId", phzSchoolId)
                    .build()
            }
            .setBasicAuthHeader(backendProperties)
            .exchange()
            .expectBodyList(UserDto::class.java)
            .returnResult()
            .responseBody

        assertThat(response).isNotNull
        assertThat(response).hasSize(1)
        assertThat(response?.first()?.id).isEqualTo(adminUserSecondSchoolId)
    }
}