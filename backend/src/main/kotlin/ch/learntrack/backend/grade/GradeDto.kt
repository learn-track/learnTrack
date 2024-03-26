package ch.learntrack.backend.grade

import java.util.UUID

public data class GradeDto(
    val id: UUID,
    val name: String,
    val schoolId: UUID,
)
