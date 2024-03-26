package ch.learntrack.backend.security

import org.springframework.security.crypto.bcrypt.BCrypt

private const val BCRYPT_SALT = 10

public class PasswordService {
    public fun isPasswordMatchingHash(password: String, hash: String): Boolean = BCrypt.checkpw(password, hash)

    public fun createPasswordHash(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_SALT))
}
