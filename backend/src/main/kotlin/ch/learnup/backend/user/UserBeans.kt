package ch.learnup.backend.user

import ch.learnup.backend.persistence.tables.daos.UserDao
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val userBeans: BeanDefinitionDsl = beans {
    bean<UserRessource>()
    bean<UserDao>()
    bean<UserService>()
}
