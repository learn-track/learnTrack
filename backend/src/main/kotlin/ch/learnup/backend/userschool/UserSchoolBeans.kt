package ch.learnup.backend.userschool

import ch.learnup.backend.persistence.tables.daos.UserSchoolDao
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val userSchoolBeans: BeanDefinitionDsl = beans {
    bean<UserSchoolDao>()
}
