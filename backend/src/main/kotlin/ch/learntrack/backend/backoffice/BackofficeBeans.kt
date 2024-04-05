package ch.learntrack.backend.backoffice

import ch.learntrack.backend.backoffice.school.SchoolRessource
import ch.learntrack.backend.backoffice.school.SchoolService
import ch.learntrack.backend.backoffice.user.UserRessource
import ch.learntrack.backend.backoffice.user.UserService
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val backofficeBeans: BeanDefinitionDsl = beans {
    bean<UserRessource>()
    bean<UserService>()
    bean<SchoolRessource>()
    bean<SchoolService>()
}
