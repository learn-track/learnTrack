package ch.learntrack.backend.utils

import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.persistence.tables.pojos.User
import ch.learntrack.backend.persistence.tables.pojos.School
import ch.learntrack.backend.persistence.tables.pojos.Grade
import ch.learntrack.backend.persistence.tables.pojos.Subject
import ch.learntrack.backend.persistence.tables.pojos.UserSchool
import ch.learntrack.backend.persistence.tables.pojos.UserGrade
import java.util.UUID

val userAdminTemplateId: UUID = UUID.fromString("40d8b918-8f80-4b92-a3f5-4548d7883c51")
val userTeacherTemplateId: UUID = UUID.fromString("40d8b918-8f80-4b92-a3f5-4548d7883c54")
val userStudentTemplateId: UUID = UUID.fromString("40d8b918-8f80-4b92-a3f5-4548d7883c55")
val schoolTemplateId: UUID = UUID.fromString("40d8b918-8f80-4b92-a3f5-4548d7883c53")
val gradeTemplateId: UUID = UUID.fromString("40d8b918-8f80-4b92-a3f5-4548d7883c52")
val subjectTemplateId: UUID = UUID.fromString("40d8b918-8f80-4b92-a3f5-4548d7883c56")

fun createAdminUserFromTemplate(
    id: UUID = userAdminTemplateId,
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

fun createTeacherUserFromTemplate(
    id: UUID = userTeacherTemplateId,
    firstName: String = "teacher",
    lastName: String = "user",
    eMail: String = "teacheruser@gmail.com",
    password: String = "\$2a\$10\$IVLBCJ8ed8zh1aYeui6Nwu4uauH/Uwtrdkd5PshFdCP9Yo0U2ltjK",
) = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    eMail = eMail,
    password = password,
    userRole = UserRole.TEACHER,
)

fun createStudentUserFromTemplate(
    id: UUID = userStudentTemplateId,
    firstName: String = "student",
    lastName: String = "user",
    eMail: String = "studentuser@gmail.com",
    password: String = "\$2a\$10\$IVLBCJ8ed8zh1aYeui6Nwu4uauH/Uwtrdkd5PshFdCP9Yo0U2ltjK",
) = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    eMail = eMail,
    password = password,
    userRole = UserRole.STUDENT
)

fun createSchoolFromTemplate(
    id: UUID = schoolTemplateId,
    name: String = "Benedict",
    address: String = "Vulkanstrasse 106",
    city: String = "Zürich",
    postcode: Int = 8048,
) = School(
    id = id,
    name = name,
    address = address,
    city = city,
    postcode = postcode,
)

fun createGradeFromTemplate(
    id: UUID = gradeTemplateId,
    name: String = "Class 1A",
    schoolId: UUID = schoolTemplateId,
) = Grade(
    id = id,
    name = name,
    schoolId = schoolId,
)

fun createSubjectFromTemplate(
    id: UUID = subjectTemplateId,
    name: String = "Math",
    gradeId: UUID = gradeTemplateId,
    teacherId: UUID = userTeacherTemplateId,
) = Subject(
    id = id,
    name = name,
    gradeId = gradeId,
    teacherId = teacherId,
)

fun createUserSchoolFromTemplate(
    userId: UUID,
    schoolId: UUID = schoolTemplateId,
) = UserSchool(
    userId = userId,
    schoolId = schoolId,
)

fun createUserGradeFromTemplate(
    userId: UUID,
    gradeId: UUID = gradeTemplateId,
) = UserGrade(
    userId = userId,
    gradeId = gradeId,
)