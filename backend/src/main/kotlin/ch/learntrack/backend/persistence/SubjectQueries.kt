package ch.learntrack.backend.persistence

import ch.learntrack.backend.persistence.tables.daos.SubjectDao
import ch.learntrack.backend.persistence.tables.pojos.Subject
import ch.learntrack.backend.persistence.tables.references.GRADE
import ch.learntrack.backend.persistence.tables.references.SUBJECT
import java.util.UUID

public fun SubjectDao.fetchSubjectsByGradeId(gradeId: UUID): List<Subject> = ctx()
    .select()
    .from(GRADE)
    .join(SUBJECT)
    .on(SUBJECT.GRADE_ID.eq(GRADE.ID))
    .where(GRADE.ID.eq(gradeId))
    .fetch()
    .into(Subject::class.java)
