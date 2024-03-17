package ch.learnup.backend.common

import org.jooq.DAO
import org.jooq.TableRecord
import org.springframework.transaction.annotation.Transactional

import java.util.UUID

@Transactional
abstract class EntityService<T, R : TableRecord<R>?, P : Any?>(val entityDao: DAO<R, P, UUID>) {
    fun findById(id: UUID): T? = entityDao.findById(id)?.let { mapToDto(it) }

    protected abstract fun mapToDto(pojo: P): T
}
