package ch.learntrack.backend.backoffice.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/backoffice/user")
public class UserRessource(private val userService: UserService) {
    @GetMapping
    public fun getAll(): List<UserDto> = userService.getAllAdminUser()
}
