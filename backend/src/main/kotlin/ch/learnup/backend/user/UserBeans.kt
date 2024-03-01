package ch.learnup.backend.user

import org.springframework.context.support.beans

val userBeans = beans {
    bean<UserRessource>()
}
