package ch.learnup.backend.school

import ch.learnup.backend.persistence.tables.daos.SchoolDao
import org.springframework.context.support.BeanDefinitionDsl

import org.springframework.context.support.beans

public val schoolBeans: BeanDefinitionDsl = beans {
    bean<SchoolDao>()
    bean<SchoolService>()
}
