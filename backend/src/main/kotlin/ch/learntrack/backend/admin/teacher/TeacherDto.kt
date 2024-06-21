package ch.learntrack.backend.admin.teacher

import java.util.UUID

public data class TeacherDto(
    val id: UUID,
    val email: String,
    val isAssignedToSchool: Boolean,
)
