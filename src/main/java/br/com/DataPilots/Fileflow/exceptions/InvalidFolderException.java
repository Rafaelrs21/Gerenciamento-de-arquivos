package br.com.DataPilots.Fileflow.exceptions;

public class InvalidFolderException extends RuntimeException {
    @Override
    public String getMessage() {
        return "A pasta é inválida.";
    }
}
