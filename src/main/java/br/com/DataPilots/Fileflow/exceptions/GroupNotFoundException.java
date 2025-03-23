package br.com.DataPilots.Fileflow.exceptions;

public class GroupNotFoundException extends RuntimeException {
    private Long groupId;

    public GroupNotFoundException(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getMessage() {
        return "Grupo com ID " + groupId + " não encontrado";
    }
}
