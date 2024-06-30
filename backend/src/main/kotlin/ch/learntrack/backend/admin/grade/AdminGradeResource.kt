package ch.learntrack.backend.admin.grade

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
@RequestMapping("$ADMIN_ROOT_URL/grade")
public class AdminGradeResource(
    private val adminGradeService: AdminGradeService,
) {
    @PreAuthorize(
        "@UserAccessAuthorizer.hasUserAccessToSchool(#root.authentication.principal, #schoolId)",
    )
    @GetMapping
    public fun getAllGradesWithInfosBySchoolId(@RequestParam schoolId: UUID): List<GradeInfoDto> =
        adminGradeService.getAllGradesForSchool(schoolId)

    @PreAuthorize(
        "@UserAccessAuthorizer.hasUserAccessToSchool(#root.authentication.principal, #schoolId)",
    )
    @PostMapping("/createGrade")
    public fun createGrade(@RequestParam schoolId: UUID, @RequestBody createGradeDto: CreateGradeDto): Unit =
        adminGradeService.createGrade(createGradeDto)

    @PreAuthorize(
        "@UserAccessAuthorizer.hasUserAccessToSchool(#root.authentication.principal, #schoolId)",
    )
    @GetMapping("/getGradeDetails")
    public fun getGradeDetails(@RequestParam schoolId: UUID, @RequestParam gradeId: UUID): GradeDetailsDto =
        adminGradeService.getGradeDetails(gradeId)
}
