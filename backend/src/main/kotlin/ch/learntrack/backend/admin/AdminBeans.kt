package ch.learntrack.backend.admin

import ch.learntrack.backend.admin.grade.gradeBeans
import ch.learntrack.backend.admin.teacher.teacherBeans
import org.springframework.context.support.BeanDefinitionDsl

public const val ADMIN_ROOT_URL: String = "/admin"

public val adminBeans: List<BeanDefinitionDsl> = listOf(
    gradeBeans,
    teacherBeans,
)
