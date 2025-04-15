package br.com.DataPilots.Fileflow.exceptions;

public class InvalidFileException extends RuntimeException {
    @Override
    public String getMessage() {
        return "O arquivo é inválido.";
    }
}
