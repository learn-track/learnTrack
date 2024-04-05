package ch.learntrack.backend.backoffice.school

import ch.learntrack.backend.persistence.tables.daos.SchoolDao
import ch.learntrack.backend.persistence.tables.pojos.School
import ch.learntrack.backend.persistence.tables.references.SCHOOL

public fun SchoolDao.fetchAllSchools(): MutableList<School> = ctx()
    .select()
    .from(SCHOOL)
    .fetch()
    .into(School::class.java)
