package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.*;
import br.com.DataPilots.Fileflow.tests.Factory;
import br.com.DataPilots.Fileflow.repositories.FileGroupPermissionRepository;
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

    @Mock
    private FileGroupPermissionRepository groupPermissionRepository;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private FilePermissionService filePermissionService;

    private FileUserPermission userPermission;
    private FileGroupPermission groupPermission;
    private File file;
    private Group group;
    private User user;

    @BeforeEach
    void setUp() {
        file = Factory.createFile(1L);
        group = Factory.createGroup(1L);
        user = Factory.createUser();

        userPermission = new FileUserPermission(file, user, Permission.VIEW);
        groupPermission = new FileGroupPermission(file, group, Permission.EDIT);
    }

    @Test
    public void getUserPermissionShouldReturnPermission() {
        when(userPermissionRepository.findById(any())).thenReturn(Optional.of(userPermission));

        Permission result = filePermissionService.getUserPermission(file.getId(), user.getId());

        assertThat(result).isEqualTo(Permission.VIEW);
    }

    @Test
    public void getGroupPermissionShouldReturnPermission() {
        when(groupPermissionRepository.findById(any())).thenReturn(Optional.of(groupPermission));

        Permission result = filePermissionService.getGroupPermission(file.getId(), group.getId());

        assertThat(result).isEqualTo(Permission.EDIT);
    }

    @Test
    public void getEffectivePermissionsShouldReturnUserPermission() {
        List<Group> groups = Collections.singletonList(group);
        when(userPermissionRepository.findById(any())).thenReturn(Optional.of(userPermission));
        when(groupService.getUserGroups(user.getId())).thenReturn(groups);
        when(groupPermissionRepository.findById(any())).thenReturn(Optional.of(groupPermission));

        Set<Permission> result = filePermissionService.getEffectivePermissions(file.getId(), user.getId());

        assertThat(result).contains(Permission.VIEW, Permission.EDIT);
    }

    @Test
    public void hasPermissionShouldReturnTrue() {
        when(userPermissionRepository.findById(any())).thenReturn(Optional.of(userPermission));
        when(groupService.getUserGroups(user.getId())).thenReturn(Collections.emptyList());

        boolean result = filePermissionService.hasPermission(file.getId(), user.getId(), Permission.VIEW);

        assertThat(result).isTrue();
    }

    @Test
    public void canEditShouldReturnFalseWhenUserDoesNotHavePermission() {
        when(userPermissionRepository.findById(any())).thenReturn(Optional.of(userPermission));
        when(groupService.getUserGroups(user.getId())).thenReturn(Collections.emptyList());

        boolean result = filePermissionService.canEdit(file.getId(), user.getId());

        assertThat(result).isFalse();
    }
}
