package br.com.DataPilots.Fileflow.exceptions;

public class InvalidShareException extends RuntimeException {
    @Override
    public String getMessage() {
        return "O compartilhamento é inválido ou expirado.";
    }
} 