package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.dtos.CreateFileShareDTO;
import br.com.DataPilots.Fileflow.dtos.SharePermissionDTO;
import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.FileShare;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.repositories.FileRepository;
import br.com.DataPilots.Fileflow.repositories.FileSharePermissionRepository;
import br.com.DataPilots.Fileflow.repositories.FileShareRepository;
import br.com.DataPilots.Fileflow.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileShareServiceTest {

    @Mock
    private FileShareRepository fileShareRepository;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FileSharePermissionRepository fileSharePermissionRepository;

    @InjectMocks
    private FileShareService fileShareService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateShareWithPermissions() {
        Long userId = 1L;
        Long fileId = 10L;

        File file = mock(File.class);
        User owner = mock(User.class);
        User targetUser = mock(User.class);

        SharePermissionDTO permissionDTO = new SharePermissionDTO();
        permissionDTO.setUserId(2L);
        permissionDTO.setCanEdit(true);
        permissionDTO.setCanShare(false);

        CreateFileShareDTO dto = new CreateFileShareDTO();
        dto.setFileId(fileId);
        dto.setPublico(false);
        dto.setTemporario(true);
        dto.setExpiresAt(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        dto.setPermissions(List.of(permissionDTO));

        when(fileRepository.findById(fileId)).thenReturn(Optional.of(file));
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(file.getUserId()).thenReturn(userId);
        when(userRepository.findById(2L)).thenReturn(Optional.of(targetUser));


        when(fileShareRepository.save(Mockito.<FileShare>any())).thenAnswer(invocation -> {
            FileShare argument = invocation.getArgument(0);
            argument.setId(100L);
            return argument;
        });

        when(fileShareRepository.findById(100L)).thenAnswer(invocation -> {
            FileShare share = new FileShare();
            share.setId(100L);
            return Optional.of(share);
        });

        FileShare result = fileShareService.createShare(dto, userId);

        assertNotNull(result);
        assertEquals(100L, result.getId());
    }

    @Test
    public void testFindSharesByUserId_UserHasShares() {
        Long userId = 1L;

        // Criando alguns compartilhamentos simulados
        FileShare share1 = mock(FileShare.class);
        FileShare share2 = mock(FileShare.class);

        List<FileShare> shares = Arrays.asList(share1, share2);

        when(fileShareRepository.findSharesByUserId(userId)).thenReturn(shares);

        List<FileShare> result = fileShareService.findSharesByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(share1, result.get(0));
        assertEquals(share2, result.get(1));
    }

    @Test
    public void testFindSharesByUserId_UserHasNoShares() {
        Long userId = 1L;

        when(fileShareRepository.findSharesByUserId(userId)).thenReturn(Collections.emptyList());
        List<FileShare> result = fileShareService.findSharesByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    public void testFindSharesCreatedByUser_UserCreatedShares() {
        Long userId = 1L;

        FileShare share1 = mock(FileShare.class);
        FileShare share2 = mock(FileShare.class);

        List<FileShare> shares = Arrays.asList(share1, share2);
        when(fileShareRepository.findSharesCreatedByUser(userId)).thenReturn(shares);
        List<FileShare> result = fileShareService.findSharesCreatedByUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(share1, result.get(0));
        assertEquals(share2, result.get(1));
    }

    @Test
    public void testFindSharesCreatedByUser_UserCreatedNoShares() {
        Long userId = 1L;

        when(fileShareRepository.findSharesCreatedByUser(userId)).thenReturn(Collections.emptyList());
        List<FileShare> result = fileShareService.findSharesCreatedByUser(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByFileId_FileHasShares() {
        Long fileId = 1L;

        FileShare share1 = mock(FileShare.class);
        FileShare share2 = mock(FileShare.class);

        List<FileShare> shares = Arrays.asList(share1, share2);
        when(fileShareRepository.findByFileId_Id(fileId)).thenReturn(shares);
        List<FileShare> result = fileShareService.findByFileId(fileId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(share1, result.get(0));
        assertEquals(share2, result.get(1));
    }


    @Test
    public void testFindByIdAndUser_OwnerAccess() {
        Long shareId = 1L;
        Long userId = 2L;

        User owner = mock(User.class);
        File file = mock(File.class);
        FileShare share = mock(FileShare.class);

        when(share.getOwner()).thenReturn(owner);
        when(owner.getId()).thenReturn(userId);
        when(share.getFile()).thenReturn(file);
        when(share.getPermissions()).thenReturn(Collections.emptyList());
        when(fileShareRepository.findById(shareId)).thenReturn(Optional.of(share));

        Optional<FileShare> result = fileShareService.findByIdAndUser(shareId, userId);

        assertTrue(result.isPresent());
        assertEquals(share, result.get());
    }

    @Test
    public void testDeleteShare_AuthorizedUser() {
        Long shareId = 5L;
        Long userId = 7L;

        FileShare share = mock(FileShare.class);
        User owner = mock(User.class);
        File file = mock(File.class);

        when(share.getOwner()).thenReturn(owner);
        when(owner.getId()).thenReturn(userId);
        when(share.getFile()).thenReturn(file);
        when(file.getUserId()).thenReturn(userId);
        when(fileShareRepository.findById(shareId)).thenReturn(Optional.of(share));

        assertDoesNotThrow(() -> fileShareService.deleteShare(shareId, userId));
        verify(fileShareRepository).deleteById(shareId);
    }



    @Test
    public void testHasSharePermission_PublicAndNotExpired() {
        Long shareId = 3L;
        Long userId = 1L;

        FileShare share = mock(FileShare.class);

        when(share.isPublico()).thenReturn(true);
        when(share.isExpired()).thenReturn(false);
        when(fileShareRepository.findById(shareId)).thenReturn(Optional.of(share));

        assertTrue(fileShareService.hasSharePermission(shareId, userId));
    }

    @Test
    public void testFindById_NotFound() {
        when(fileShareRepository.findById(999L)).thenReturn(Optional.empty());
        assertTrue(fileShareService.findById(999L).isEmpty());
    }

    @Test
    public void testFindByPublicToken() {
        String token = "mockedToken";
        String seed = "mockedSeed";

        FileShare fileShare = mock(FileShare.class);
        when(fileShare.isPublico()).thenReturn(true);
        when(fileShare.getPublicToken()).thenReturn(token);
        when(fileShare.getShareSeed()).thenReturn(seed);
        when(fileShare.getId()).thenReturn(1L);

        when(fileShareRepository.findAllPublicAndNotExpired()).thenReturn(List.of(fileShare));

        Optional<FileShare> result = fileShareService.findByPublicToken(token);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    public void testFindByIdAndUser_ShareNotFound() {
        Long shareId = 1L;
        Long userId = 2L;

        when(fileShareRepository.findById(shareId)).thenReturn(Optional.empty());

        Optional<FileShare> result = fileShareService.findByIdAndUser(shareId, userId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByIdAndUser_UserIsNotOwnerNorFileUser() {
        Long shareId = 1L;
        Long userId = 2L;

        User owner = mock(User.class);
        User fileUser = mock(User.class);
        File file = mock(File.class);
        FileShare share = mock(FileShare.class);

        when(share.getOwner()).thenReturn(owner);
        when(owner.getId()).thenReturn(3L);
        when(share.getFile()).thenReturn(file);
        when(file.getUserId()).thenReturn(4L);
        when(share.getPermissions()).thenReturn(Collections.emptyList());
        when(fileShareRepository.findById(shareId)).thenReturn(Optional.of(share));

        Optional<FileShare> result = fileShareService.findByIdAndUser(shareId, userId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByIdAndUser_UserHasNoPermission() {
        Long shareId = 1L;
        Long userId = 2L;

        User owner = mock(User.class);
        User fileUser = mock(User.class);
        File file = mock(File.class);
        FileShare share = mock(FileShare.class);

        when(share.getOwner()).thenReturn(owner);
        when(owner.getId()).thenReturn(3L);
        when(share.getFile()).thenReturn(file);
        when(file.getUserId()).thenReturn(4L);

        when(share.getPermissions()).thenReturn(Collections.emptyList());
        when(fileShareRepository.findById(shareId)).thenReturn(Optional.of(share));

        Optional<FileShare> result = fileShareService.findByIdAndUser(shareId, userId);
        assertFalse(result.isPresent());
    }
}