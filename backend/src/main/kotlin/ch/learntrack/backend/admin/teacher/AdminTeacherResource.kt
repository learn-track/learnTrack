package ch.learntrack.backend.admin.teacher

import ch.learntrack.backend.admin.ADMIN_ROOT_URL
import ch.learntrack.backend.user.UserDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("$ADMIN_ROOT_URL/teacher")
public class AdminTeacherResource(private val adminTeacherService: AdminTeacherService) {
    @PreAuthorize(
        "@UserAccessAuthorizer.hasUserAccessToSchool(#root.authentication.principal, #schoolId)",
    )
    @GetMapping
    public fun getAllTeachersForSchool(@RequestParam schoolId: UUID): List<UserDto> =
        adminTeacherService.getAllTeachersForSchool(schoolId)

    @PreAuthorize(
        "@UserAccessAuthorizer.hasUserAccessToSchool(#root.authentication.principal, #schoolId)",
    )
    @GetMapping("/search")
    public fun searchTeacherByEmail(@RequestParam schoolId: UUID, @RequestParam email: String): List<TeacherDto> =
        adminTeacherService.searchTeacherByEmail(schoolId, email)

    @PreAuthorize(
        "@UserAccessAuthorizer.hasUserAccessToSchool(#root.authentication.principal, #schoolId)",
    )
    @PutMapping("/assignTeacherToSchool")
    public fun assignTeacherToSchool(
        @RequestParam schoolId: UUID,
        @RequestParam teacherId: UUID): Unit =
        adminTeacherService.assignTeacherToSchool(schoolId, teacherId)
}
