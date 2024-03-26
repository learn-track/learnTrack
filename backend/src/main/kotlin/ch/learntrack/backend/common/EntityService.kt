package ch.learntrack.backend.common

import org.jooq.DAO
import org.jooq.TableRecord
import org.springframework.transaction.annotation.Transactional

import java.util.UUID

@Transactional
public abstract class EntityService<T, R : TableRecord<R>?, P : Any?>(private val entityDao: DAO<R, P, UUID>) {
    public fun findById(id: UUID): T? = entityDao.findById(id)?.let { mapToDto(it) }

    protected abstract fun mapToDto(pojo: P): T
}
