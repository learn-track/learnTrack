package ch.learntrack.backend.admin

import ch.learntrack.backend.admin.grade.CreateGradeDto
import ch.learntrack.backend.admin.grade.GradeService
import ch.learntrack.backend.persistence.tables.pojos.Grade
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@PreAuthorize(
    "@UserAccessAuthorizer.isAdminUser(#root.authentication.principal)",
)
@RestController
@RequestMapping("/admin")
public class AdminRessource(
    private val gradeService: GradeService,
) {
    @PostMapping("/createGrade")
    public fun createGrade(createGradeDto: CreateGradeDto): Grade = gradeService.createGrade(createGradeDto)
}
