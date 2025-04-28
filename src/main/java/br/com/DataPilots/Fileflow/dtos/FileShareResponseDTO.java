package br.com.DataPilots.Fileflow.dtos;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.FileShare;
import br.com.DataPilots.Fileflow.entities.FileSharePermission;
import br.com.DataPilots.Fileflow.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileShareResponseDTO {
    private Long id;
    private Instant createdAt;
    private Instant expiresAt;
    private UserDTO owner;
    private File file;
    private String shareSeed;
    private List<PermissionDTO> permissions;
    private boolean publico;
    private boolean temporario;
    private boolean expirado;
    private String publicToken;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        private Long id;
        private String username;

        public static UserDTO fromUser(User user) {
            return new UserDTO(
                user.getId(),
                user.getUsername()
            );
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PermissionDTO {
        private Long userId;
        private boolean canEdit;
        private boolean canShare;

        public static PermissionDTO fromPermission(FileSharePermission permission) {
            return new PermissionDTO(
                permission.getUser().getId(),
                permission.isCanEdit(),
                permission.isCanShare()
            );
        }
    }

    public static FileShareResponseDTO fromFileShare(FileShare share) {
        List<PermissionDTO> permissions = share.getPermissions() != null ?
            share.getPermissions().stream()
                .map(PermissionDTO::fromPermission)
                .collect(Collectors.toList()) :
            List.of();

        return new FileShareResponseDTO(
            share.getId(),
            share.getCreatedAt(),
            share.getExpiresAt(),
            UserDTO.fromUser(share.getOwner()),
            share.getFile(),
            share.getShareSeed(),
            permissions,
            share.isPublico(),
            share.isTemporario(),
            share.isExpired(),
            share.getPublicToken()
        );
    }
} 