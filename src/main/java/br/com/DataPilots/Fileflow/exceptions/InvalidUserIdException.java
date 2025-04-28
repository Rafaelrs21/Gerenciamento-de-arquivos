package br.com.DataPilots.Fileflow.exceptions;

public class InvalidUserIdException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Usuário não encontrado";
    }
}
