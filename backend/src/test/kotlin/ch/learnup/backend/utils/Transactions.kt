package ch.learnup.backend.utils

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

inline fun <T> PlatformTransactionManager.runInTransaction(crossinline function: () -> T): T =
    requireNotNull(TransactionTemplate(this).execute { function() })