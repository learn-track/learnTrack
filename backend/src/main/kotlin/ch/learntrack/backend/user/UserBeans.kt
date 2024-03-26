package ch.learntrack.backend.user

import ch.learntrack.backend.persistence.tables.daos.UserDao
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val userBeans: BeanDefinitionDsl = beans {
    bean<UserRessource>()
    bean<UserDao>()
    bean<UserService>()
}
