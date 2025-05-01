package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.*;
import br.com.DataPilots.Fileflow.tests.Factory;
import br.com.DataPilots.Fileflow.repositories.FileUserPermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilePermissionServiceTest {

    @Mock
    private FileUserPermissionRepository userPermissionRepository;

    @InjectMocks
    private FilePermissionService filePermissionService;

    private FileUserPermission userPermission;
    private File file;
    private User user;

    @BeforeEach
    void setUp() {
        file = Factory.createFile();
        user = Factory.createUser("test", "password");

        userPermission = new FileUserPermission(file, user, Permission.VIEW);
    }

    @Test
    public void getUserPermissionShouldReturnPermission() {
        when(userPermissionRepository.findById(any())).thenReturn(Optional.of(userPermission));

        Permission result = filePermissionService.getUserPermission(file.getId(), user.getId());

        assertThat(result).isEqualTo(Permission.VIEW);
    }

    @Test
    public void hasPermissionShouldReturnTrue() {
        when(userPermissionRepository.findById(any())).thenReturn(Optional.of(userPermission));

        boolean result = filePermissionService.hasPermission(file.getId(), user.getId(), Permission.VIEW);

        assertThat(result).isTrue();
    }

    @Test
    public void canEditShouldReturnFalseWhenUserDoesNotHavePermission() {
        when(userPermissionRepository.findById(any())).thenReturn(Optional.of(userPermission));

        boolean result = filePermissionService.canEdit(file.getId(), user.getId());

        assertThat(result).isFalse();
    }
}
