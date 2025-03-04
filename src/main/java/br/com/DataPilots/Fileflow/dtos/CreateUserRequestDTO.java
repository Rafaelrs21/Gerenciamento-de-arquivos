package br.com.DataPilots.Fileflow.dtos;

import br.com.DataPilots.Fileflow.AppConsts;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDTO(
    @NotBlank
    String username,
    @Size(min=AppConsts.MIN_PASSWORD_LENGTH)
    String password
) {
}