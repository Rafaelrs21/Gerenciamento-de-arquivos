package br.com.DataPilots.Fileflow.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFileShareDTO {
    @NotNull
    private Long fileId;
    private List<SharePermissionDTO> permissions;
    private boolean publico;
    private boolean temporario;
    private Instant expiresAt;
} 