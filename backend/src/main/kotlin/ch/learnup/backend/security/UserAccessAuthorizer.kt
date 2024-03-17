package ch.learnup.backend.security

import ch.learnup.backend.persistence.tables.daos.UserDao

class UserAccessAuthorizer(private val userDao: UserDao) {
    fun isUser(myUserDetails: LearnupUserDetails): Boolean = myUserDetails.username == "testuser@gmail.com"
}
