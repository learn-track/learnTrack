package ch.learnup.backend.whoami

import ch.learnup.backend.grade.GradeService
import ch.learnup.backend.school.SchoolService
import ch.learnup.backend.security.LearnupUserDetails
import ch.learnup.backend.user.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/whoami")
class WhoamiRessource(
    private val userService: UserService,
    private val gradeService: GradeService,
    private val schoolService: SchoolService,
) {
    @GetMapping
    fun getSelf(@AuthenticationPrincipal principal: LearnupUserDetails): WhoamiDto {
        val user = userService.mapToDto(principal.user)

        return WhoamiDto(
            user = user,
            grades = gradeService.getGradesForUserByUserId(user.id),
            schools = schoolService.getSchoolsForUserByUserId(user.id),
        )
    }
}
