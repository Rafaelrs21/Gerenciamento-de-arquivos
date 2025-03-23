package br.com.DataPilots.Fileflow.exceptions;

public class UserNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Usuário não encontrado.";
    }
}
