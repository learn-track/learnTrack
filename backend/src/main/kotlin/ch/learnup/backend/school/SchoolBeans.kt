package ch.learnup.backend.school

import ch.learnup.backend.persistence.tables.daos.SchoolDao

import org.springframework.context.support.beans

val schoolBeans = beans {
    bean<SchoolDao>()
    bean<SchoolService>()
}
