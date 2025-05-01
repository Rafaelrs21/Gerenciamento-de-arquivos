package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.*;
import br.com.DataPilots.Fileflow.repositories.FileRepository;
import br.com.DataPilots.Fileflow.repositories.FileUserPermissionRepository;
import br.com.DataPilots.Fileflow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilePermissionService {
    private final FileUserPermissionRepository userPermissionRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public Permission getUserPermission(Long fileId, Long userId) {
        var pk = new FileUserPermission.FileUserPermissionPK(fileId, userId);
        return userPermissionRepository.findById(pk)
            .map(FileUserPermission::getPermission)
            .orElse(null);
    }

    public boolean hasPermission(Long fileId, Long userId, Permission requiredPermission) {
        return getUserPermission(fileId, userId).equals(requiredPermission);
    }

    public FileUserPermission grantUserPermission(Long fileId, Long userId, Permission permission) {
        Optional<File> file = fileRepository.findById(fileId);
        Optional<User> user = userRepository.findById(userId);

        if (file.isEmpty() || user.isEmpty()) return null;

        var filePermission = new FileUserPermission(file.get(), user.get(), permission);
        return userPermissionRepository.save(filePermission);
    }

    public void revokeUserPermission(Long fileId, Long userId) {
        var pk = new FileUserPermission.FileUserPermissionPK(fileId, userId);
        userPermissionRepository.deleteById(pk);
    }

    public boolean canView(Long fileId, Long userId) {
        return hasPermission(fileId, userId, Permission.VIEW);
    }

    public boolean canEdit(Long fileId, Long userId) {
        return hasPermission(fileId, userId, Permission.EDIT);
    }

    public boolean canDelete(Long fileId, Long userId) {
        return hasPermission(fileId, userId, Permission.DELETE);
    }
}
