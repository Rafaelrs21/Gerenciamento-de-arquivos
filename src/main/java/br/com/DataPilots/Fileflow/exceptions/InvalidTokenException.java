package br.com.DataPilots.Fileflow.exceptions;

public class InvalidTokenException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Token inválido ou expirado.";
    }
}