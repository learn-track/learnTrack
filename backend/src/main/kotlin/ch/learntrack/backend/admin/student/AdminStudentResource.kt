package ch.learntrack.backend.admin.student

import ch.learntrack.backend.admin.ADMIN_ROOT_URL
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("$ADMIN_ROOT_URL/student")
public class AdminStudentResource(private val adminStudentService: AdminStudentService) {
    @PreAuthorize(
        "@UserAccessAuthorizer.hasUserAccessToSchool(#root.authentication.principal, #schoolId)",
    )
    @GetMapping
    public fun getAllStudentsWithDetailsBySchoolId(@RequestParam schoolId: UUID): List<StudentDetailsDto> =
        adminStudentService.getAllStudentsWithDetailsForSchool(schoolId)

    @PreAuthorize(
        "@UserAccessAuthorizer.hasUserAccessToSchool(#root.authentication.principal, #schoolId)",
    )
    @PostMapping("/createStudent")
    public fun createStudent(@RequestParam schoolId: UUID, @RequestBody createStudentDto: CreateStudentDto): Unit =
        adminStudentService.createStudent(createStudentDto)
}
