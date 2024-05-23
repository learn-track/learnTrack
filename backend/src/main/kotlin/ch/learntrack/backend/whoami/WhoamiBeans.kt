package ch.learntrack.backend.whoami

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val whoamiBeans: BeanDefinitionDsl = beans {
    bean<WhoamiResource>()
}
