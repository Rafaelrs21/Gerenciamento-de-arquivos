package br.com.DataPilots.Fileflow.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecoverPasswordRequestDTO(
    @Email @NotBlank
    String email
) {
}
