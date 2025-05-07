package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.entities.FileUserPermission;
import br.com.DataPilots.Fileflow.entities.Permission;
import br.com.DataPilots.Fileflow.services.FilePermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

public class FilePermissionControllerTest {
    private FilePermissionService filePermissionService;

    private FilePermissionController filePermissionController;

    @BeforeEach
    public void setUp() {
        filePermissionService = Mockito.mock(FilePermissionService.class);
        filePermissionController = new FilePermissionController(filePermissionService);
    }

    @Test
    public void getUserPermission() {
        Long fileId = 1L;
        Long userId = 2L;

        Permission permission = Mockito.mock(Permission.class);

        Mockito.when(filePermissionService.getUserPermission(fileId, userId)).thenReturn(permission);

        var response = filePermissionController.getUserPermission(fileId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(permission, response.getBody());
    }

    @Test
    public void grantPermission() {
        Long fileId = 1L;
        Long userId = 2L;
        Permission permission = Mockito.mock(Permission.class);
        FileUserPermission filePermission = Mockito.mock(FileUserPermission.class);

        Mockito.when(filePermissionService.grantUserPermission(fileId, userId, permission)).thenReturn(filePermission);

        var response = filePermissionController.grantPermission(fileId, userId, permission);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(filePermission, response.getBody());
    }

    @Test
    public void revokePermission() {
        Long fileId = 1L;
        Long userId = 2L;

        var response = filePermissionController.revokePermission(fileId, userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Mockito.verify(filePermissionService, Mockito.times(1)).revokeUserPermission(fileId, userId);
    }
}
