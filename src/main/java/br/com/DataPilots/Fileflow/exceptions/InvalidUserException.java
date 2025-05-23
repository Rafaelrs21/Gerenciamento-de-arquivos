package br.com.DataPilots.Fileflow.exceptions;

public abstract class InvalidUserException extends Exception {
    @Override
    public String getMessage() {
        return "Usuário não existe";
    }
}