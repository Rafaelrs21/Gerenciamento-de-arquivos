package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.*;
import br.com.DataPilots.Fileflow.repositories.FileRepository;
import br.com.DataPilots.Fileflow.repositories.UserRepository;
import br.com.DataPilots.Fileflow.tests.Factory;
import br.com.DataPilots.Fileflow.repositories.FileUserPermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilePermissionServiceTest {
    private FileUserPermissionRepository userPermissionRepository;
    private FileRepository fileRepository;
    private UserRepository userRepository;

    private FilePermissionService filePermissionService;

    private FileUserPermission userPermission;
    private File file;
    private User user;

    @BeforeEach
    void setUp() {
        userPermissionRepository = Mockito.mock(FileUserPermissionRepository.class);
        fileRepository = Mockito.mock(FileRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        filePermissionService = new FilePermissionService(userPermissionRepository, fileRepository, userRepository);

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

    @Test
    public void grantUserPermission() {
        File file = Mockito.mock(File.class);
        User user = Mockito.mock(User.class);

        Mockito.when(fileRepository.findById(any())).thenReturn(Optional.of(file));
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));

        filePermissionService.grantUserPermission(file.getId(), user.getId(), Permission.VIEW);

        Mockito.verify(userPermissionRepository, Mockito.times(1)).save(Mockito.any(FileUserPermission.class));
    }

    @Test
    public void revokeUserPermission() {
        filePermissionService.revokeUserPermission(file.getId(), user.getId());
        Mockito.verify(userPermissionRepository, Mockito.times(1)).deleteById(Mockito.any(FileUserPermission.FileUserPermissionPK.class));
    }

    @Test
    public void canView() {
        when(userPermissionRepository.findById(any())).thenReturn(Optional.of(userPermission));

        var result = filePermissionService.canView(file.getId(), user.getId());
        assertEquals(true, result);
    }

    @Test
    public void canEdit() {
        when(userPermissionRepository.findById(any())).thenReturn(Optional.of(userPermission));

        var result = filePermissionService.canEdit(file.getId(), user.getId());
        assertEquals(false, result);
    }

    @Test
    public void canDelete() {
        when(userPermissionRepository.findById(any())).thenReturn(Optional.of(userPermission));

        var result = filePermissionService.canDelete(file.getId(), user.getId());
        assertEquals(false, result);
    }
}
