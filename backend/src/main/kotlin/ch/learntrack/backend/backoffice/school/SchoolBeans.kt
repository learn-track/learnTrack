package ch.learntrack.backend.backoffice.school

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val schoolBeans: BeanDefinitionDsl = beans {
    bean<SchoolRessource>()
    bean<SchoolService>()
}
