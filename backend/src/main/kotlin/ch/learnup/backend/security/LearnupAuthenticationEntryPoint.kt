package ch.learnup.backend.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class LearnupAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException?,
    ) {
        if (authException is InsufficientAuthenticationException) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Invalid Token")
        } else {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Unknown Authorization Exception")
        }
    }
}
