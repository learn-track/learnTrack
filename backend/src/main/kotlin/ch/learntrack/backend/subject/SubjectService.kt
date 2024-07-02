package ch.learntrack.backend.subject

import ch.learntrack.backend.common.EntityService
import ch.learntrack.backend.persistence.fetchSubjectsByGradeId
import ch.learntrack.backend.persistence.tables.daos.SubjectDao
import ch.learntrack.backend.persistence.tables.pojos.Subject
import ch.learntrack.backend.persistence.tables.records.SubjectRecord
import java.util.UUID

public class SubjectService(
    private val subjectDao: SubjectDao,
) : EntityService<SubjectDto, SubjectRecord, Subject>(subjectDao) {
    public override fun mapToDto(pojo: Subject): SubjectDto = SubjectDto(
        id = pojo.id,
        name = pojo.name,
    )

    public fun getSubjectsByGradeId(gradeId: UUID): List<SubjectDto> =
        subjectDao.fetchSubjectsByGradeId(gradeId).map(::mapToDto)
}
