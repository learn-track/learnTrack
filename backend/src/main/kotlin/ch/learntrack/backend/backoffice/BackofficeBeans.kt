package ch.learntrack.backend.backoffice

import ch.learntrack.backend.backoffice.school.schoolBeans
import ch.learntrack.backend.backoffice.user.userBeans

import org.springframework.context.support.BeanDefinitionDsl

public const val BACKOFFICE_ROOT_URL: String = "/backoffice"

public val backofficeBeans: List<BeanDefinitionDsl> = listOf(
    userBeans,
    schoolBeans,
)
