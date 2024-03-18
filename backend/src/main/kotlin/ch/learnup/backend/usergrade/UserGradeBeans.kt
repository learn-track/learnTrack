package ch.learnup.backend.usergrade

import ch.learnup.backend.persistence.tables.daos.UserGradeDao
import org.springframework.context.support.beans

val userGradeBeans = beans {
    bean<UserGradeDao>()
}
