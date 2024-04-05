package ch.learntrack.backend.backoffice.school

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/backoffice/school")
public class SchoolRessource(private val schoolService: SchoolService) {
    @GetMapping
    public fun getAllSchools(): List<SchoolDto> = schoolService.getAllSchools()
}
