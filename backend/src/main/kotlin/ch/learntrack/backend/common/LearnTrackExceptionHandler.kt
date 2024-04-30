package ch.learntrack.backend.common

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
public class LearnTrackExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(LearnTrackAuthorizationException::class)
    public fun handleUnauthorized(
        ex: LearnTrackAuthorizationException,
        request: WebRequest,
    ): ResponseEntity<Any>? = handleExceptionInternal(ex, ex.message, HttpHeaders(), HttpStatus.UNAUTHORIZED, request)

    @ExceptionHandler(LearnTrackConflictException::class)
    public fun handleConflict(
        ex: LearnTrackConflictException,
        request: WebRequest,
    ): ResponseEntity<Any>? = handleExceptionInternal(ex, ex.message, HttpHeaders(), HttpStatus.CONFLICT, request)

    @ExceptionHandler(LearnTrackBadRequestException::class)
    public fun handleBadRequest(
        ex: LearnTrackBadRequestException,
        request: WebRequest,
    ): ResponseEntity<Any>? = handleExceptionInternal(ex, ex.message, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
}
