package ch.learntrack.backend.security

import ch.learntrack.backend.persistence.UserRole
import ch.learntrack.backend.school.SchoolService
import java.util.UUID

public class UserAccessAuthorizer(
    private val schoolService: SchoolService,
) {
    public fun isAdminUser(myUserDetails: LearnTrackUserDetails): Boolean = myUserDetails.user.userRole ==
            UserRole.ADMIN

    public fun hasUserAccessToSchool(myUserDetails: LearnTrackUserDetails, schoolId: UUID): Boolean =
        schoolService.hasUserAccessToSchool(myUserDetails.user.id, schoolId)
}
