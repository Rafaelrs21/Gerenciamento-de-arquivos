package br.com.DataPilots.Fileflow.dtos;

public record UpdatePasswordRequestDTO(String recoveryToken, String newPassword) {
}
