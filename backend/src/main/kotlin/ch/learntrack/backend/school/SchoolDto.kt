package ch.learntrack.backend.school

import java.util.UUID

public data class SchoolDto(
    val id: UUID,
    val name: String,
    val address: String,
    val city: String,
    val postcode: Int,
)
