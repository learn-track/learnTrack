package ch.learntrack.backend.school

import ch.learntrack.backend.persistence.tables.daos.SchoolDao
import org.springframework.context.support.BeanDefinitionDsl

import org.springframework.context.support.beans

public val schoolBeans: BeanDefinitionDsl = beans {
    bean<SchoolDao>()
    bean<SchoolService>()
}
