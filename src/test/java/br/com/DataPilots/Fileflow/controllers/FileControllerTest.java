package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.dtos.DefaultResponseDTO;
import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.InvalidFileException;
import br.com.DataPilots.Fileflow.services.FileService;
import br.com.DataPilots.Fileflow.tests.Factory;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FileControllerTest {
    private FileService fileService;
    private FileController fileController;

    @BeforeEach
    void setUp() {
        fileService = Mockito.mock(FileService.class);

        fileController = new FileController(fileService);
    }

    @Test
    public void createFile_ok() throws Exception {
        File file = Factory.createFile();
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);

        ResponseEntity<DefaultResponseDTO> response = fileController.createFile(user, file);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Arquivo criado.", response.getBody().message());
    }

    @Test
    public void createFile_withError() {
        File file = Factory.createFile();
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);

        Mockito.doThrow(new InvalidFileException()).when(fileService).create(file.getName(), file.getMimeType(), file.getBase64(), 1L, 0L);

        ResponseEntity<DefaultResponseDTO> response = fileController.createFile(user, file);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void downloadFile_ok() {
        User user = Mockito.mock(User.class);
        Long folderId = 1010L;
        String fileName = "teste.txt";
        Mockito.when(user.getId()).thenReturn(1L);

        Mockito.when(fileService.downloadFile(fileName, 1L, folderId)).thenReturn(Faker.instance().lorem().characters(120));

        ResponseEntity<byte[]> response = fileController.downloadFile(user, folderId.toString(), fileName);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void downloadFile_whenFolderIdNull_ok() {
        User user = Mockito.mock(User.class);
        String fileName = "teste.txt";
        Mockito.when(user.getId()).thenReturn(1L);

        Mockito.when(fileService.downloadFile(fileName, 1L, 0L)).thenReturn(Faker.instance().lorem().characters(120));

        ResponseEntity<byte[]> response = fileController.downloadFile(user, "null", fileName);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void downloadFile_whenError() {
        User user = Mockito.mock(User.class);
        String fileName = "teste.txt";
        Mockito.when(user.getId()).thenReturn(1L);

        Mockito.when(fileService.downloadFile(fileName, 1L, 0L)).thenThrow(new InvalidFileException());

        assertThrows(InvalidFileException.class, () -> {
            fileController.downloadFile(user, "null", fileName);
        });
    }

    @Test
    public void getFilesByUser_ok() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);

        File file = Factory.createFile();
        List<File> files = new ArrayList<>();
        files.add(file);

        Mockito.when(fileService.getFilesByUser(1L)).thenReturn(files);

        var response = fileController.getFilesByUser(user);

        assertEquals(1, response.size());
        assertEquals(file.getId(), response.get(0).get("id"));
        assertEquals(file.getName(), response.get(0).get("name"));
        assertEquals(file.getMimeType(), response.get(0).get("mimeType"));
        assertEquals(file.getBase64(), response.get(0).get("base64"));
        assertEquals(file.getSize(), response.get(0).get("size"));
        assertEquals(file.getCreatedAt(), response.get(0).get("createdAt"));
        assertEquals(file.getUserId(), response.get(0).get("userId"));
        assertEquals(file.getFolderId(), response.get(0).get("folderId"));
    }

    @Test
    public void deleteFile_ok() {
        File file = Mockito.mock(File.class);
        Mockito.when(file.getUserId()).thenReturn(1L);
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Long folderId = 1010L;
        String fileName = "teste.txt";


        Mockito.when(fileService.findByNameAndUserIdAndFolderId(fileName, user.getId(), folderId)).thenReturn(Optional.of(file));

        var response = fileController.deleteFile(user, folderId, fileName);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Arquivo deletado.", response.getBody().message());
    }

    @Test
    public void deleteFile_whenNotFound() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Long folderId = 1010L;
        String fileName = "teste.txt";


        Mockito.when(fileService.findByNameAndUserIdAndFolderId(fileName, user.getId(), folderId)).thenReturn(Optional.empty());

        var response = fileController.deleteFile(user, folderId, fileName);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Arquivo não encontrado.", response.getBody().message());
    }

    @Test
    public void deleteFile_whenUserIsNotOwner() {
        File file = Mockito.mock(File.class);
        Mockito.when(file.getUserId()).thenReturn(2L);
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Long folderId = 1010L;
        String fileName = "teste.txt";


        Mockito.when(fileService.findByNameAndUserIdAndFolderId(fileName, user.getId(), folderId)).thenReturn(Optional.of(file));

        var response = fileController.deleteFile(user, folderId, fileName);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Você não tem permissão para excluir este arquivo.", response.getBody().message());
    }
}
