package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.*;
import br.com.DataPilots.Fileflow.repositories.FileGroupPermissionRepository;
import br.com.DataPilots.Fileflow.repositories.FileUserPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilePermissionService {
    private final FileUserPermissionRepository userPermissionRepository;
    private final FileGroupPermissionRepository groupPermissionRepository;
    private final GroupService groupService;

    public Permission getUserPermission(Long fileId, Long userId) {
        var pk = new FileUserPermission.FileUserPermissionPK(fileId, userId);
        return userPermissionRepository.findById(pk)
            .map(FileUserPermission::getPermission)
            .orElse(null);
    }

    public Permission getGroupPermission(Long fileId, Long groupId) {
        var pk = new FileGroupPermission.FileGroupPermissionPK(fileId, groupId);
        return groupPermissionRepository.findById(pk)
            .map(FileGroupPermission::getPermission)
            .orElse(null);
    }

    // Verifica as permissões do usuário e dos grupos aos quais ele pertence
    public Set<Permission> getEffectivePermissions(Long fileId, Long userId) {
        Optional<Permission> userPermission = Optional.ofNullable(getUserPermission(fileId, userId));

        Set<Permission> groupPermissions = groupService.getUserGroups(userId).stream()
            .map(group -> getGroupPermission(fileId, group.getId()))
            .filter(permission -> permission != null)
            .collect(Collectors.toSet());

        userPermission.ifPresent(groupPermissions::add);

        return groupPermissions;
    }

    public boolean hasPermission(Long fileId, Long userId, Permission requiredPermission) {
        return getEffectivePermissions(fileId, userId).contains(requiredPermission);
    }

    public FileUserPermission grantUserPermission(File file, User user, Permission permission) {
        var filePermission = new FileUserPermission(file, user, permission);
        return userPermissionRepository.save(filePermission);
    }

    public FileGroupPermission grantGroupPermission(File file, Group group, Permission permission) {
        var filePermission = new FileGroupPermission(file, group, permission);
        return groupPermissionRepository.save(filePermission);
    }

    public void revokeUserPermission(Long fileId, Long userId) {
        var pk = new FileUserPermission.FileUserPermissionPK(fileId, userId);
        userPermissionRepository.deleteById(pk);
    }

    public void revokeGroupPermission(Long fileId, Long groupId) {
        var pk = new FileGroupPermission.FileGroupPermissionPK(fileId, groupId);
        groupPermissionRepository.deleteById(pk);
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
