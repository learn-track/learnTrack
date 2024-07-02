package ch.learntrack.backend.subject

import java.util.UUID

public data class SubjectDto(
    val id: UUID,
    val name: String,
    val gradeId: UUID,
    val teacherId: UUID?,
)
