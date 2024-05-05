package ch.learntrack.backend.email

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val emailBeans: BeanDefinitionDsl = beans {
    bean<MailSender>()
}
