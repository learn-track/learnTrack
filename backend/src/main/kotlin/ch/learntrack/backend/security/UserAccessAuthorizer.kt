package ch.learntrack.backend.security

import ch.learntrack.backend.persistence.tables.daos.UserDao

public class UserAccessAuthorizer(private val userDao: UserDao) {
    public fun isUser(myUserDetails: LearnTrackUserDetails): Boolean = myUserDetails.username == "testuser@gmail.com"
}
