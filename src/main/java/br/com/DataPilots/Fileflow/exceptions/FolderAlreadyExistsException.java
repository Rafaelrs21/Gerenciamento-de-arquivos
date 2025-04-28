package br.com.DataPilots.Fileflow.exceptions;

public class FolderAlreadyExistsException extends InvalidFileException {
    @Override
    public String getMessage() {
        return "A pasta já existe.";
    }
}
