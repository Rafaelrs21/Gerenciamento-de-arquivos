package br.com.DataPilots.Fileflow.exceptions;

public class PasswordRecoveryInvalidOrExpiredException extends Exception {
    @Override
    public String getMessage() {
        return "Recuração de senha inválida ou expirada.";
    }
}
