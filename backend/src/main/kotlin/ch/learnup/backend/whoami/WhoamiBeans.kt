package ch.learnup.backend.whoami

import org.springframework.context.support.beans

val whoamiBeans = beans {
    bean<WhoamiRessource>()
}
