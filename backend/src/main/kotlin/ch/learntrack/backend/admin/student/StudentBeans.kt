package ch.learntrack.backend.admin.student

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val studentBeans: BeanDefinitionDsl = beans {
    bean<AdminStudentResource>()
    bean<AdminStudentService>()
}
