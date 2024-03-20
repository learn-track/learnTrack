package ch.learnup.backend.jwt

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.context.support.beans

public val jwtBeans: BeanDefinitionDsl = beans {
    bean<TokenService>()
}
