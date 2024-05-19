package ch.learntrack.backend.admin.grade

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val gradeBeans: BeanDefinitionDsl = beans {
    bean<GradeResource>()
    bean<GradeService>()
}
