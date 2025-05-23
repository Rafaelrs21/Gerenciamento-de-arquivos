package br.com.DataPilots.Fileflow.exceptions;

public class SharePermissionDeniedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Você não tem permissão para acessar este compartilhamento.";
    }
} 