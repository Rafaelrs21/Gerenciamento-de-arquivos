package br.com.DataPilots.Fileflow.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFileShareDTO {
    @NotBlank
    private Long fileId;
    @NotBlank
    private Long ownerId;
    private List<Long> userIds;
    private boolean canEdit;
    private boolean canShare;
    private boolean isPublic;
    private boolean isTemporary;
    private Instant expiresAt;
} 