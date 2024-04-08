package ch.learntrack.backend.backoffice.school

import ch.learntrack.backend.IntegrationTest
import ch.learntrack.backend.persistence.tables.references.SCHOOL
import ch.learntrack.backend.utils.createSchoolFromTemplate
import ch.learntrack.backend.utils.deleteAll
import ch.learntrack.backend.utils.runInTransaction
import ch.learntrack.backend.utils.schoolTemplateId
import ch.learntrack.backend.utils.setBasicAuthHeader
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.BodyInserters
import java.util.UUID

private const val ETH_SCHOOL = "45ffeb7e-ce1c-46a2-8415-fe2b4196c12c"

class SchoolIntegrationTest: IntegrationTest() {
    private val newNameSchoolId = UUID.fromString(ETH_SCHOOL)

    @BeforeEach
    fun setUp() {
        transactionManager.runInTransaction {
            schoolDao.insert(createSchoolFromTemplate())
            schoolDao.insert(createSchoolFromTemplate(id = newNameSchoolId, name = "ETH"))
        }
    }

    @AfterEach
    fun cleanUp() {
        transactionManager.runInTransaction {
            schoolDao.deleteAll()
        }
    }

    @Test
    fun `should get 401 with wrong credentials`() {
        webClient.get()
            .uri("/backoffice/school")
            .setBasicAuthHeader("wrong", "credentials")
            .exchange()
            .expectStatus()
            .isUnauthorized
    }

    @Test
    fun `should return a list with schools`() {
        val response = webClient.get()
            .uri("/backoffice/school")
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
            .uri("/backoffice/school/create")
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
            .uri("/backoffice/school/create")
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
            .uri("/backoffice/school/create")
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
}