package com.sigeg.exceptions;

/**
 * Exceção lançada para violações de regras de negócio do SIGEG.
 * Tratada pelo GlobalExceptionHandler com HTTP 422 Unprocessable Entity.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
