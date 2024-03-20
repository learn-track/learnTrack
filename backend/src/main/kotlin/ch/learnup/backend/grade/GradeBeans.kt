package ch.learnup.backend.grade

import ch.learnup.backend.persistence.tables.daos.GradeDao
import org.springframework.context.support.BeanDefinitionDsl

import org.springframework.context.support.beans

public val gradeBeans: BeanDefinitionDsl = beans {
    bean<GradeDao>()
    bean<GradeService>()
}
