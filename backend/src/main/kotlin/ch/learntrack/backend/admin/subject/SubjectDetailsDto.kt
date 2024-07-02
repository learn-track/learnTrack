package ch.learntrack.backend.admin.subject

import ch.learntrack.backend.subject.SubjectDto
import ch.learntrack.backend.user.UserDto

public data class SubjectDetailsDto(
    val subject: SubjectDto,
    val teacher: UserDto?,
)
