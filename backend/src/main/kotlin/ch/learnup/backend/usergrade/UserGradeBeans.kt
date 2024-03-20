package ch.learnup.backend.usergrade

import ch.learnup.backend.persistence.tables.daos.UserGradeDao
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val userGradeBeans: BeanDefinitionDsl = beans {
    bean<UserGradeDao>()
}
