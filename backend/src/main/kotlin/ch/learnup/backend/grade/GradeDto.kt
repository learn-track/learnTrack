package ch.learnup.backend.grade

import java.util.UUID

data class GradeDto(
    val id: UUID,
    val name: String,
    val schoolId: UUID,
)
