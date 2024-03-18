package ch.learnup.backend.userschool

import ch.learnup.backend.persistence.tables.daos.UserSchoolDao
import org.springframework.context.support.beans

val userSchoolBeans = beans {
    bean<UserSchoolDao>()
}
