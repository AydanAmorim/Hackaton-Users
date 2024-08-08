package com.payment.system.authentication_service.util.exceptions;

import com.payment.system.authentication_service.util.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerUtil {

    @ExceptionHandler({Exception.class, ExternalInterfaceException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<StandardError> genericError(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status)
                .body(new StandardError(status.value(),
                        "Erro desconhecido",
                        e.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<StandardError> notAuthorized(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        return ResponseEntity.status(status)
                .body(new StandardError(status.value(),
                        "Acesso Bloqueado",
                        e.getMessage(),
                        request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<StandardError> dataValidations(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        String message;

        if (e.getFieldError() != null && e.getFieldError().getDefaultMessage() != null) {
            message = MessageUtil.getMessage(e.getFieldError().getDefaultMessage());

            if (message == null) {
                message = e.getFieldError().getField() + ": " + e.getFieldError().getDefaultMessage();
            }
        } else {
            message = e.getMessage();
        }

        StandardError error = new StandardError(status.value(),
                "Possível erro de payload",
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardError> validationsError(BusinessException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        StandardError error = new StandardError(status.value(),
                "Bloqueado por regra de negócio",
                exception.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }
}
