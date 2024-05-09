package ch.learntrack.backend.admin

import ch.learntrack.backend.admin.grade.GradeService
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val adminBeans: BeanDefinitionDsl = beans {
    bean<AdminRessource>()
    bean<GradeService>()
}
