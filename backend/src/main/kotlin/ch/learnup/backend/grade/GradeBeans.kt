package ch.learnup.backend.grade

import ch.learnup.backend.persistence.tables.daos.GradeDao

import org.springframework.context.support.beans

val gradeBeans = beans {
    bean<GradeDao>()
    bean<GradeService>()
}
