package br.com.DataPilots.Fileflow.exceptions;

public class InvalidFolderPermissionException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Você não tem permissão excluir essa pasta.";
    }
}
