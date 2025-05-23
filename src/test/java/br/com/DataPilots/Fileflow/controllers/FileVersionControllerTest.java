package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.FileVersion;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.repositories.FileRepository;
import br.com.DataPilots.Fileflow.services.FileVersionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FileVersionControllerTest {
    private FileVersionService fileVersionService;
    private FileRepository fileRepository;

    private FileVersionController fileVersionController;

    @BeforeEach
    public void setUp() {
        fileRepository = Mockito.mock(FileRepository.class);
        fileVersionService = Mockito.mock(FileVersionService.class);

        fileVersionController = new FileVersionController(fileVersionService, fileRepository);
    }

    @Test
    public void getAllVersionsByFileId() {
        User user = Mockito.mock(User.class);
        Long userId = 1L;
        Long fileId = 2L;
        File file = Mockito.mock(File.class);

        List<FileVersion> versions = new ArrayList<>();
        versions.add(Mockito.mock(FileVersion.class));

        Mockito.when(user.getId()).thenReturn(userId);
        Mockito.when(file.getUserId()).thenReturn(userId);
        Mockito.when(fileRepository.findById(fileId)).thenReturn(Optional.of(file));
        Mockito.when(fileVersionService.listVersionsByFileId(fileId)).thenReturn(versions);

        var response = fileVersionController.getAllVersionsByFileId(user, fileId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(versions, response.getBody());
    }

    @Test
    public void getAllVersionsByFileId_whenNotFound() {
        User user = Mockito.mock(User.class);
        Long fileId = 2L;

        Mockito.when(fileRepository.findById(fileId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> fileVersionController.getAllVersionsByFileId(user, fileId));
        assertEquals("Arquivo não encontrado", exception.getMessage());
    }

    @Test
    public void getAllVersionsByFileId_whenUserIsNotFileOwner() {
        User user = Mockito.mock(User.class);
        Long userId = 1L;
        Long fileId = 2L;
        File file = Mockito.mock(File.class);

        Mockito.when(user.getId()).thenReturn(userId);
        Mockito.when(file.getUserId()).thenReturn(3L);
        Mockito.when(fileRepository.findById(fileId)).thenReturn(Optional.of(file));

        var response = fileVersionController.getAllVersionsByFileId(user, fileId);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getVersionById() {
        User user = Mockito.mock(User.class);
        Long versionId = 1L;
        Long userId = 2L;
        FileVersion version = Mockito.mock(FileVersion.class);
        File file = Mockito.mock(File.class);

        Mockito.when(user.getId()).thenReturn(userId);
        Mockito.when(file.getUserId()).thenReturn(userId);
        Mockito.when(version.getFile()).thenReturn(file);
        Mockito.when(fileVersionService.getVersionById(versionId)).thenReturn(version);

        var response = fileVersionController.getVersionById(user, versionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(version, response.getBody());
    }

    @Test
    public void getVersionById_whenUserIsNotFileOwne() {
        User user = Mockito.mock(User.class);
        Long versionId = 1L;
        Long userId = 2L;
        FileVersion version = Mockito.mock(FileVersion.class);
        File file = Mockito.mock(File.class);

        Mockito.when(user.getId()).thenReturn(userId);
        Mockito.when(file.getUserId()).thenReturn(3L);
        Mockito.when(version.getFile()).thenReturn(file);
        Mockito.when(fileVersionService.getVersionById(versionId)).thenReturn(version);

        var response = fileVersionController.getVersionById(user, versionId);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getLatestVersion() {
        User user = Mockito.mock(User.class);
        Long userId = 1L;
        Long fileId = 2L;
        File file = Mockito.mock(File.class);
        FileVersion version = Mockito.mock(FileVersion.class);

        Mockito.when(user.getId()).thenReturn(userId);
        Mockito.when(file.getUserId()).thenReturn(userId);
        Mockito.when(fileRepository.findById(fileId)).thenReturn(Optional.of(file));
        Mockito.when(fileVersionService.getLatestVersion(file)).thenReturn(version);

        var response = fileVersionController.getLatestVersion(user, fileId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(version, response.getBody());
    }

    @Test
    public void getLatestVersion_w() {
        User user = Mockito.mock(User.class);
        Long fileId = 2L;

        Mockito.when(fileRepository.findById(fileId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> fileVersionController.getLatestVersion(user, fileId));
        assertEquals("Arquivo não encontrado", exception.getMessage());
    }

    @Test
    public void getLatestVersion_whenUserIsNotFileOwner() {
        User user = Mockito.mock(User.class);
        Long userId = 1L;
        Long fileId = 2L;
        File file = Mockito.mock(File.class);
        FileVersion version = Mockito.mock(FileVersion.class);

        Mockito.when(user.getId()).thenReturn(userId);
        Mockito.when(file.getUserId()).thenReturn(3L);
        Mockito.when(fileRepository.findById(fileId)).thenReturn(Optional.of(file));

        var response = fileVersionController.getLatestVersion(user, fileId);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
