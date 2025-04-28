package br.com.DataPilots.Fileflow.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateFolderRequestDTO(
    @NotBlank(message = "Nome da pasta é obrigatório")
    String name
) {
}
