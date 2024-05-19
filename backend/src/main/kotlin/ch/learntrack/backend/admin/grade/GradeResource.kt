package ch.learntrack.backend.admin.grade

import ch.learntrack.backend.admin.ADMIN_ROOT_URL
import ch.learntrack.backend.persistence.tables.pojos.Grade
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

// TODO: Replace with crude role based access control
@PreAuthorize(
    "@UserAccessAuthorizer.isAdminUser(#root.authentication.principal)",
)
@RestController
@RequestMapping("$ADMIN_ROOT_URL/grade")
public class GradeResource(
    private val gradeService: GradeService,
) {
    @PreAuthorize(
        "@UserAccessAuthorizer.hasUserAccessToSchool(#root.authentication.principal, #schoolId)",
    )
    @PostMapping("/createGrade")
    public fun createGrade(@RequestParam schoolId: UUID, @RequestBody createGradeDto: CreateGradeDto): Grade =
        gradeService.createGrade(
            createGradeDto,
        )
}
