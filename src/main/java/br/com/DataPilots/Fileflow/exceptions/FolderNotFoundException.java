package br.com.DataPilots.Fileflow.exceptions;

public class FolderNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "A pasta não foi encontrada.";
    }
}
