package br.com.DataPilots.Fileflow.exceptions;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
        super();
    }

    public FileNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage() != null ? super.getMessage() : "Arquivo não encontrado.";
    }
} 