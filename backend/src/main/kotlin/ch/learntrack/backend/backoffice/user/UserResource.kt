package ch.learntrack.backend.backoffice.user

import ch.learntrack.backend.backoffice.BACKOFFICE_ROOT_URL
import ch.learntrack.backend.persistence.UserRole

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("$BACKOFFICE_ROOT_URL/user")
public class UserResource(private val userService: UserService) {
    @GetMapping
    public fun getAllAdminUsers(): List<UserDto> = userService.getAllAdminUsers()

    @PostMapping("/create")
    public fun createAdminUser(@RequestBody createUserDto: CreateUserDto): Unit =
        userService.createAdminUser(createUserDto)

    @GetMapping("/getAllAdminsBySchoolId")
    public fun getAllAdminsForSchool(@RequestParam schoolId: UUID): List<UserDto> = userService.getAllAdminsBySchoolId(
        schoolId,
    )

    @GetMapping("/getTeacherCount")
    public fun getTeacherCounts(@RequestParam schoolId: UUID): Int = userService.getUserCount(schoolId,
        UserRole.TEACHER)

    @GetMapping("/getStudentCount")
    public fun getStudentCounts(@RequestParam schoolId: UUID): Int = userService.getUserCount(schoolId,
        UserRole.STUDENT)

    @GetMapping("/getAllAdminUsersNotAssignedToSchool")
    public fun getAllAdminUsersNotAssignedToSchool(@RequestParam schoolId: UUID): List<UserDto> =
        userService.getAllAdminUsersNotAssignedToSchool(schoolId)
}
