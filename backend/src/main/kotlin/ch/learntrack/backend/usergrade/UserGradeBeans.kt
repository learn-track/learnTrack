package ch.learntrack.backend.usergrade

import ch.learntrack.backend.persistence.tables.daos.UserGradeDao
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val userGradeBeans: BeanDefinitionDsl = beans {
    bean<UserGradeDao>()
}
