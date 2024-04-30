package ch.learntrack.backend.utils

import org.apache.commons.text.StringEscapeUtils

// Regular expression for email validation. It checks if the input is in the format of an email.
private const val EMAIL_REGEX = """^[\w-.]+@([\w-]+\.)+[\w-]{2,}${'$'}"""

// Regular expression for password validation. It checks if the password contains at least one digit,
// one uppercase letter, one lowercase letter, one special character, and is at least 8 characters long.
private const val PASSWORD_REGEX = """^(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\w\d\s:])([^\s]){8,}${'$'}"""

public fun String.isEmailValid(): Boolean = EMAIL_REGEX.toRegex().containsMatchIn(this)

public fun String.isPasswordValid(): Boolean = PASSWORD_REGEX.toRegex().containsMatchIn(this)

public fun String.sanitizeInputString(): String = StringEscapeUtils.escapeHtml4(
    StringEscapeUtils.escapeEcmaScript(replace("'", "''")).trim(),
)
