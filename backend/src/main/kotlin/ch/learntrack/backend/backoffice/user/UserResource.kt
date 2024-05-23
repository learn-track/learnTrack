package ch.learntrack.backend.backoffice.user

import ch.learntrack.backend.backoffice.BACKOFFICE_ROOT_URL
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$BACKOFFICE_ROOT_URL/user")
public class UserResource(private val userService: UserService) {
    @GetMapping
    public fun getAllAdminUsers(): List<UserDto> = userService.getAllAdminUsers()

    @PostMapping("/create")
    public fun createAdminUser(@RequestBody createUserDto: CreateUserDto) {
        userService.createAdminUser(createUserDto)
    }
}
