package ch.learnup.backend.security

import ch.learnup.backend.common.LearnupAuthorizationException
import ch.learnup.backend.jwt.TokenService
import ch.learnup.backend.persistence.tables.daos.UserDao
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

private const val TOKEN_SUBSTRING = 7

public class LearnupUserDetailService(
    private val userDao: UserDao,
    private val tokenService: TokenService,
) : AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    override fun loadUserDetails(token: PreAuthenticatedAuthenticationToken?): UserDetails {
        token ?: throw UsernameNotFoundException("No token received")

        try {
            val tokenUser = tokenService.parseIdFromToken(token.principal.toString().substring(TOKEN_SUBSTRING))

            val user = userDao.fetchOneById(tokenUser)
                ?: throw UsernameNotFoundException("User not found for id ${token.principal}")

            return LearnupUserDetails(user)
        } catch (e: LearnupAuthorizationException) {
            throw AuthenticationCredentialsNotFoundException("invalid token", e)
        }
    }
}
