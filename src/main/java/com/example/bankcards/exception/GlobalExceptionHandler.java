package com.example.bankcards.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handlerIoException(IOException e) {
        log.error("Service unavailable due to IO exception: {}", e.getMessage());
        return buildResponse(e, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception e) {
        log.error("Service unavailable due to Exception: {}", e.getMessage());
        return buildResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handlerIllegalArgumentException(IllegalArgumentException e) {
        log.error("Service unavailable due to IllegalArgumentException: {}", e.getMessage());
        return buildResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handlerBadCredentialsException(BadCredentialsException e) {
        log.error("Service unavailable due to BadCredentialsException: {}", e.getMessage());
        return buildResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<?> handlerAuthenticationCredentialsNotFoundException(
            AuthenticationCredentialsNotFoundException e
    ) {
        log.error("Service unavailable due to AuthenticationCredentialsNotFoundException: {}", e.getMessage());
        return buildResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handlerRuntimeException(RuntimeException e) {
        log.error("Service unavailable due to RuntimeException: {}", e.getMessage());
        return buildResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handlerUsernameNotFoundException(UsernameNotFoundException e) {
        log.error("Service unavailable due to UsernameNotFoundException: {}", e.getMessage());
        return buildResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handlerDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("Service unavailable due to DataIntegrityViolationException: {}", e.getMessage());
        return buildResponse(e, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handlerNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("Service unavailable due to NoHandlerFoundException: {}", e.getMessage());
        return buildResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> handlerMalformedJwtException(MalformedJwtException e) {
        log.error("Service unavailable due to MalformedJwtException: {}", e.getMessage());
        return buildResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handlerExpiredJwtException(ExpiredJwtException e) {
        log.error("Service unavailable due to ExpiredJwtException: {}", e.getMessage());
        return buildResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> handlerSignatureException(SignatureException e) {
        log.error("Service unavailable due to SignatureException: {}", e.getMessage());
        return buildResponse(e, HttpStatus.UNAUTHORIZED);
    }


    private ResponseEntity<ExceptionDto> buildResponse(Exception e, HttpStatus status) {
        ExceptionDto dto = ExceptionDto.builder()
                .success(false)
                .message(e.getMessage())
                .errorType(e.getClass().getSimpleName())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(dto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getMessage();

        log.error("Service unavailable due to MethodArgumentNotValidException: {}", message);

        Map<String, String> errors = e.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Objects.requireNonNullElse(
                                fieldError.getDefaultMessage(), "Invalid field"
                        )
                ));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(MethodArgumentNotValidExceptionDto.create("Request validation failed", errors));
    }
}
