package ch.learntrack.backend.backoffice.user

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val userBeans: BeanDefinitionDsl = beans {
    bean<UserRessource>()
    bean<UserService>()
}
