package ch.learnup.backend.security

import ch.learnup.backend.persistence.tables.daos.UserDao

public class UserAccessAuthorizer(private val userDao: UserDao) {
    public fun isUser(myUserDetails: LearnupUserDetails): Boolean = myUserDetails.username == "testuser@gmail.com"
}
