package ch.learnup.backend.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserRessource {
    @GetMapping
    fun test(): UserDto = UserDto(name = "test string from backend")
}
