package ch.learntrack.backend.subject

import ch.learntrack.backend.persistence.tables.daos.SubjectDao
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val subjectBeans: BeanDefinitionDsl = beans {
    bean<SubjectDao>()
}
