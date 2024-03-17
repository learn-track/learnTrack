package ch.learnup.backend.utils

import ch.learnup.backend.persistence.UserRole
import ch.learnup.backend.persistence.tables.pojos.User
import java.util.UUID

val userTemplateId: UUID = UUID.fromString("40d8b918-8f80-4b92-a3f5-4548d7883c51")

fun createUserFromTemplate(
    id: UUID = userTemplateId,
    firstName: String = "test",
    lastName: String = "user",
    eMail: String = "testuser@gmail.com",
    password: String = "\$2a\$10\$IVLBCJ8ed8zh1aYeui6Nwu4uauH/Uwtrdkd5PshFdCP9Yo0U2ltjK",
) = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    eMail = eMail,
    password = password,
    userRole = UserRole.ADMIN,
)