package ch.learntrack.backend.admin.teacher

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val teacherBeans: BeanDefinitionDsl = beans {
    bean<TeacherResource>()
    bean<AdminTeacherService>()
}
