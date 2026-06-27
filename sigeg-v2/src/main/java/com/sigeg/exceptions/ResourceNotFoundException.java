package com.sigeg.exceptions;

import java.util.UUID;

/**
 * Exceção lançada quando um recurso não é encontrado no banco de dados.
 * Tratada pelo GlobalExceptionHandler com HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, UUID id) {
        super(String.format("%s com id '%s' não encontrado.", resourceName, id));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
