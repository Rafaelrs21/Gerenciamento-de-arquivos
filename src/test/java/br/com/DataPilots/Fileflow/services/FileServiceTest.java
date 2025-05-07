package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.FileVersion;
import br.com.DataPilots.Fileflow.exceptions.FileAlreadyExistsException;
import br.com.DataPilots.Fileflow.exceptions.InvalidFileException;
import br.com.DataPilots.Fileflow.repositories.FileRepository;
import br.com.DataPilots.Fileflow.repositories.FileVersionRepository;
import br.com.DataPilots.Fileflow.tests.Factory;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class FileServiceTest {
    private FileRepository repository;
    private FileVersionRepository fileVersionRepository;
    private FileService fileService;

    @BeforeEach
    public void setUp() {
        repository = Mockito.mock(FileRepository.class);
        fileVersionRepository = Mockito.mock(FileVersionRepository.class);
        fileService = new FileService(repository, fileVersionRepository);
    }

    @Test
    public void create_whenSuccess() {
        File file = Factory.createFile();
        List<File> existingFiles = List.of();

        Mockito.when(repository.findByUserIdAndFolderId(1L, 2L)).thenReturn(existingFiles);

        fileService.create(file.getName(), file.getMimeType(), file.getBase64(), 1L, 2L);

        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(File.class));
    }

    @Test
    public void create_whenBase64Invalid() {
        File file = Factory.createFile();
        List<File> existingFiles = List.of();

        Mockito.when(repository.findByUserIdAndFolderId(1L, 2L)).thenReturn(existingFiles);

        assertThrows(InvalidFileException.class, () -> {
            fileService.create(file.getName(), file.getMimeType(), "", 1L, 2L);
        });
    }

    @Test
    public void create_whenFileNameInUse() {
        File file = Factory.createFile();
        List<File> existingFiles = List.of(file);

        Mockito.when(repository.findByUserIdAndFolderId(1L, 2L)).thenReturn(existingFiles);

        assertThrows(FileAlreadyExistsException.class, () -> {
            fileService.create(file.getName(), file.getMimeType(), file.getBase64(), 1L, 2L);
        });
    }

    @Test
    public void downloadFile_whenSuccess() {
        File file = Factory.createFile();

        Mockito.when(repository.findByNameAndUserIdAndFolderId(file.getName(), 1L, 2L)).thenReturn(Optional.of(file));

        var result = fileService.downloadFile(file.getName(), 1L, 2L);

        assertEquals(file.getBase64(), result);
    }

    @Test
    public void downloadFile_whenFileDoesNotExist() {
        String fileName = "teste.txt";
        Mockito.when(repository.findByNameAndUserIdAndFolderId(fileName, 1L, 2L)).thenReturn(Optional.empty());

        assertThrows(InvalidFileException.class, () -> {
            fileService.downloadFile(fileName, 1L, 2L);
        });
    }

    @Test
    public void getFilesByUser() {
        File file = Factory.createFile();

        Mockito.when(repository.findByUserId(1L)).thenReturn(List.of(file));

        var result = fileService.getFilesByUser(1L);
        assertEquals(1, result.size());
        assertEquals(file.getName(), result.get(0).getName());
    }

    @Test
    public void deleteFile() {
        File file = Factory.createFile();

        fileService.delete(file);

        Mockito.verify(repository, Mockito.times(1)).delete(file);
    }

    @Test
    public void updateFile() {
        File file = Mockito.mock(File.class);
        Long userId = 10L;
        String fileName = "teste.txt";
        String mimeType = "text/plain";
        String base64 = Base64.getEncoder().encodeToString(Faker.instance().lorem().characters(50, 100).getBytes());

        Mockito.when(repository.findById(file.getId())).thenReturn(Optional.of(file));
        Mockito.when(file.getUserId()).thenReturn(userId);
        Mockito.when(file.getName()).thenReturn(fileName);
        Mockito.when(file.getMimeType()).thenReturn(mimeType);
        Mockito.when(file.getBase64()).thenReturn(base64);

        fileService.updateFile(file, 10L);

        Mockito.verify(fileVersionRepository, Mockito.times(1)).save(Mockito.any(FileVersion.class));
        Mockito.verify(file, Mockito.times(1)).setName(fileName);
        Mockito.verify(file, Mockito.times(1)).setMimeType(mimeType);
        Mockito.verify(file, Mockito.times(1)).setBase64(base64);
        Mockito.verify(file, Mockito.times(1)).setSize(Mockito.anyLong());
        Mockito.verify(file, Mockito.times(1)).setCreatedAt(Mockito.any(Timestamp.class));
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(File.class));
    }

    @Test
    public void updateFile_whenFileDoesNotExist() {
        File file = Mockito.mock(File.class);

        Mockito.when(repository.findById(file.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> fileService.updateFile(file, 10L));
        assertEquals("Arquivo não encontrado", exception.getMessage());
    }

    @Test
    public void updateFile_whenDifferentUserId() {
        File file = Mockito.mock(File.class);
        String fileName = "teste.txt";
        String mimeType = "text/plain";
        String base64 = Faker.instance().lorem().characters(50, 100);

        Mockito.when(repository.findById(file.getId())).thenReturn(Optional.of(file));
        Mockito.when(file.getUserId()).thenReturn(1010L);
        Mockito.when(file.getName()).thenReturn(fileName);
        Mockito.when(file.getMimeType()).thenReturn(mimeType);
        Mockito.when(file.getBase64()).thenReturn(base64);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> fileService.updateFile(file, 10L));

        assertEquals("Usuário sem permissão", exception.getMessage());

        Mockito.verify(fileVersionRepository, Mockito.never()).save(Mockito.any(FileVersion.class));
        Mockito.verify(file, Mockito.never()).setName(fileName);
        Mockito.verify(file, Mockito.never()).setMimeType(mimeType);
        Mockito.verify(file, Mockito.never()).setBase64(base64);
        Mockito.verify(file, Mockito.never()).setSize(Mockito.anyLong());
        Mockito.verify(file, Mockito.never()).setCreatedAt(Mockito.any(Timestamp.class));
        Mockito.verify(repository, Mockito.never()).save(Mockito.any(File.class));
    }

    @Test
    public void getFilesByFolder() {
        Long userId = 10L;
        Long folderId = 1L;
        fileService.getFilesByFolder(userId, folderId);
        Mockito.verify(repository, Mockito.times(1)).findByUserIdAndFolderId(userId, folderId);
    }
}