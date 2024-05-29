package ch.learntrack.backend.admin.teacher

import ch.learntrack.backend.admin.ADMIN_ROOT_URL
import ch.learntrack.backend.user.UserDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("$ADMIN_ROOT_URL/teacher")
public class TeacherResource(private val teacherService: TeacherService) {
    @PreAuthorize(
        "@UserAccessAuthorizer.hasUserAccessToSchool(#root.authentication.principal, #schoolId)",
    )
    @GetMapping
    public fun getAllTeachersForSchool(@RequestParam schoolId: UUID): List<UserDto> =
        teacherService.getAllTeachersForSchool(schoolId)
}
