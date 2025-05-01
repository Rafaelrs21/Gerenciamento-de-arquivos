package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.entities.FileUserPermission;
import br.com.DataPilots.Fileflow.entities.Permission;
import br.com.DataPilots.Fileflow.services.FilePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file-permission")
@RequiredArgsConstructor
public class FilePermissionController {
    private final FilePermissionService filePermissionService;

    @GetMapping("/{fileId}/user/{userId}")
    public ResponseEntity<Permission> getUserPermission(@PathVariable Long fileId, @PathVariable Long userId) {
        Permission permission = filePermissionService.getUserPermission(fileId, userId);
        return ResponseEntity.ok(permission);
    }

    @PostMapping("/grant")
    public ResponseEntity<FileUserPermission> grantPermission(
            @RequestParam Long fileId,
            @RequestParam Long userId,
            @RequestParam Permission permission) {
        FileUserPermission filePermission = filePermissionService.grantUserPermission(fileId, userId, permission);
        return ResponseEntity.ok(filePermission);
    }

    @DeleteMapping("/{fileId}/user/{userId}")
    public ResponseEntity<Void> revokePermission(@PathVariable Long fileId, @PathVariable Long userId) {
        filePermissionService.revokeUserPermission(fileId, userId);
        return ResponseEntity.noContent().build();
    }
}
