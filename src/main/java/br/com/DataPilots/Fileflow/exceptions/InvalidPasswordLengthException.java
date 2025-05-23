package br.com.DataPilots.Fileflow.exceptions;

public class InvalidPasswordLengthException extends InvalidUserException {
    @Override
    public String getMessage() {
        return "Tamanho da senha inválido.";
    }
}