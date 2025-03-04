package br.com.DataPilots.Fileflow.exceptions;

public class UserAlreadyExistsException extends InvalidUserException {
    @Override
    public String getMessage() {
        return "Esse usuário já existe.";
    }
}