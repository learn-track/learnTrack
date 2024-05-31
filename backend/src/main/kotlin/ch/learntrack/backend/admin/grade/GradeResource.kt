package ch.learntrack.backend.admin.grade

import ch.learntrack.backend.admin.ADMIN_ROOT_URL
import ch.learntrack.backend.grade.CreateGradeDto
import ch.learntrack.backend.grade.GradeDto
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("$ADMIN_ROOT_URL/grade")
public class GradeResource(
    private val adminGradeService: AdminGradeService,
) {
    @PreAuthorize(
        "@UserAccessAuthorizer.hasUserAccessToSchool(#root.authentication.principal, #schoolId)",
    )
    @GetMapping
    public fun getGrades(@RequestParam schoolId: UUID): GradeDetailsDto =
        adminGradeService.getGrades(schoolId)

    @PreAuthorize(
        "@UserAccessAuthorizer.hasUserAccessToSchool(#root.authentication.principal, #schoolId)",
    )
    @PostMapping("/createGrade")
    public fun createGrade(@RequestParam schoolId: UUID, @RequestBody createGradeDto: CreateGradeDto): GradeDto =
        adminGradeService.createGrade(
            createGradeDto,
        )
}
