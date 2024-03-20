package ch.learnup.backend.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter

public class UserAuthorizationFilter(authorizingUserDetailService: LearnupUserDetailService,
) : RequestHeaderAuthenticationFilter() {
    init {
        setExceptionIfHeaderMissing(false)
        setPrincipalRequestHeader("Authorization")
        val provider = PreAuthenticatedAuthenticationProvider()
        provider.setPreAuthenticatedUserDetailsService(authorizingUserDetailService)
        setAuthenticationManager(ProviderManager(provider))
    }

    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest): String? =
        super.getPreAuthenticatedPrincipal(request) as? String
}
