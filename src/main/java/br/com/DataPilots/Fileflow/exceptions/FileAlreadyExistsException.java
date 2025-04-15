package br.com.DataPilots.Fileflow.exceptions;

public class FileAlreadyExistsException extends InvalidFileException {
    @Override
    public String getMessage() {
        return "Esse arquivo já existe.";
    }
}
