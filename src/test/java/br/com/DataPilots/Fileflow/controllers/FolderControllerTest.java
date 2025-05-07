package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.dtos.CreateFolderRequestDTO;
import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.services.FileService;
import br.com.DataPilots.Fileflow.services.FolderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FolderControllerTest {
    private FolderService folderService;
    private FileService fileService;

    private FolderController folderController;

    @BeforeEach
    public void setUp() {
        folderService = Mockito.mock(FolderService.class);
        fileService = Mockito.mock(FileService.class);

        folderController = new FolderController(folderService, fileService);
    }

    @Test
    public void createFolder() {
        String folderName = "testFolder";
        User user = Mockito.mock(User.class);
        CreateFolderRequestDTO body = new CreateFolderRequestDTO(folderName);

        Mockito.when(user.getId()).thenReturn(1L);

        var response = folderController.createFolder(user, body);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Pasta criada.", response.getBody().message());
        Mockito.verify(folderService).create(user.getId(), folderName);
    }

    @Test
    public void createFolder_whenSomeException() {
        String folderName = "testFolder";
        User user = Mockito.mock(User.class);
        CreateFolderRequestDTO body = new CreateFolderRequestDTO(folderName);

        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.doThrow(new RuntimeException("Ocorreu um erro")).when(folderService).create(1L, folderName);

        var response = folderController.createFolder(user, body);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ocorreu um erro", response.getBody().message());
    }

    @Test
    public void deleteFolder() {
        User user = Mockito.mock(User.class);
        Long folderId = 1L;

        Mockito.when(user.getId()).thenReturn(1L);

        var response = folderController.deleteFolder(user, folderId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pasta deletada.", response.getBody().message());
        Mockito.verify(folderService).delete(user.getId(), folderId);
    }

    @Test
    public void deleteFolder_whenSomeException() {
        User user = Mockito.mock(User.class);
        Long folderId = 10L;

        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.doThrow(new RuntimeException("Ocorreu um erro")).when(folderService).delete(1L, folderId);

        var response = folderController.deleteFolder(user, folderId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ocorreu um erro", response.getBody().message());
    }

    @Test
    public void getFilesByFolder() {
        User user = Mockito.mock(User.class);
        Long folderId = 10L;

        List<File> files = new ArrayList<>();
        File file = Mockito.mock(File.class);
        files.add(file);

        Map<String, Object> data = Map.of("teste1", "teste2");
        Mockito.when(file.serialize()).thenReturn(data);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(fileService.getFilesByFolder(1L, folderId)).thenReturn(files);

        var response = folderController.getFilesByFolder(user, folderId);
        assertEquals(response.get(0), data);
    }
}
