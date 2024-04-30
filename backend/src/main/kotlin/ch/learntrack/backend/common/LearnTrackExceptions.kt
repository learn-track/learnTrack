package ch.learntrack.backend.common

public class LearnTrackAuthorizationException(message: String) : Exception(message)

public class LearnTrackForbiddenException(message: String) : Exception(message)

public class LearnTrackConflictException(message: String) : Exception(message)

public class LearnTrackBadRequestException(message: String) : Exception(message)
