package ch.learnup.backend.utils

import org.jooq.UpdatableRecord
import org.jooq.impl.DAOImpl


/**
 *  * This function executes a cascading delete. Be careful while using it!
 **/
fun <R : UpdatableRecord<R>, P> DAOImpl<R, P, *>.deleteAll() = ctx().truncate(table).cascade().execute()
