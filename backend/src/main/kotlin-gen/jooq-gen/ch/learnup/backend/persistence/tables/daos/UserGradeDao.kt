/*
 * This file is generated by jOOQ.
 */
package ch.learnup.backend.persistence.tables.daos


import ch.learnup.backend.persistence.tables.UserGradeTable
import ch.learnup.backend.persistence.tables.pojos.UserGrade
import ch.learnup.backend.persistence.tables.records.UserGradeRecord

import java.util.UUID

import kotlin.collections.List

import org.jooq.Configuration
import org.jooq.Record2
import org.jooq.impl.DAOImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class UserGradeDao(configuration: Configuration?) : DAOImpl<UserGradeRecord, UserGrade, Record2<UUID?, UUID?>>(UserGradeTable.USER_GRADE, UserGrade::class.java, configuration) {

    /**
     * Create a new UserGradeDao without any configuration
     */
    constructor(): this(null)

    override fun getId(o: UserGrade): Record2<UUID?, UUID?> = compositeKeyRecord(o.userId, o.gradeId)

    /**
     * Fetch records that have <code>user_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfUserId(lowerInclusive: UUID, upperInclusive: UUID): List<UserGrade> = fetchRange(UserGradeTable.USER_GRADE.USER_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>user_id IN (values)</code>
     */
    fun fetchByUserId(vararg values: UUID): List<UserGrade> = fetch(UserGradeTable.USER_GRADE.USER_ID, *values)

    /**
     * Fetch records that have <code>grade_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfGradeId(lowerInclusive: UUID, upperInclusive: UUID): List<UserGrade> = fetchRange(UserGradeTable.USER_GRADE.GRADE_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>grade_id IN (values)</code>
     */
    fun fetchByGradeId(vararg values: UUID): List<UserGrade> = fetch(UserGradeTable.USER_GRADE.GRADE_ID, *values)
}