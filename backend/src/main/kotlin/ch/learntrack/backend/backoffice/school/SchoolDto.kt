package ch.learntrack.backend.backoffice.school

import java.time.LocalDateTime
import java.util.UUID

public data class SchoolDto(
    val id: UUID,
    val name: String,
    val address: String,
    val city: String,
    val postcode: Int,
    val created: LocalDateTime?,
    val updated: LocalDateTime?,
)

public data class CreateSchoolDto(
    val name: String,
    val address: String,
    val city: String,
    val postcode: Int,
)
