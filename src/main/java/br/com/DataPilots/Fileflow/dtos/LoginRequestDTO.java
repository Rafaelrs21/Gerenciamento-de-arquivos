package br.com.DataPilots.Fileflow.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank
    String username,
    @NotBlank
    String password
) {
}
