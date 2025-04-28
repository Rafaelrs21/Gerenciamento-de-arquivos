package br.com.DataPilots.Fileflow.exceptions;

public class InvalidShareException extends RuntimeException {
    public InvalidShareException() {
        super();
    }

    public InvalidShareException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage() != null ? super.getMessage() : "O compartilhamento é inválido ou expirado.";
    }
} 