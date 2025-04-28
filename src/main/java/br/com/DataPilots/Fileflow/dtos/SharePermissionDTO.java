package br.com.DataPilots.Fileflow.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharePermissionDTO {
    private Long userId;
    private boolean canEdit;
    private boolean canShare;
} 