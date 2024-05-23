package ch.learntrack.backend.whoami

import ch.learntrack.backend.grade.GradeService
import ch.learntrack.backend.school.SchoolService
import ch.learntrack.backend.security.LearnTrackUserDetails
import ch.learntrack.backend.user.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/whoami")
public class WhoamiResource(
    private val userService: UserService,
    private val gradeService: GradeService,
    private val schoolService: SchoolService,
) {
    @GetMapping
    public fun getSelf(@AuthenticationPrincipal principal: LearnTrackUserDetails): WhoamiDto {
        val user = userService.mapToDto(principal.user)

        return WhoamiDto(
            user = user,
            grades = gradeService.getGradesForUserByUserId(user.id),
            schools = schoolService.getSchoolsForUserByUserId(user.id),
        )
    }
}
