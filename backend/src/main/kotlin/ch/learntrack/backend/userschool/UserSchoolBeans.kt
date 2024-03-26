package ch.learntrack.backend.userschool

import ch.learntrack.backend.persistence.tables.daos.UserSchoolDao
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val userSchoolBeans: BeanDefinitionDsl = beans {
    bean<UserSchoolDao>()
}
