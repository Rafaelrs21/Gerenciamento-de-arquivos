package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.exceptions.InvalidFileException;
import br.com.DataPilots.Fileflow.repositories.FileRepository;
import br.com.DataPilots.Fileflow.repositories.FileVersionRepository;
import br.com.DataPilots.Fileflow.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

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

        assertThrows(InvalidFileException.class, () -> {
            fileService.create(file.getName(), file.getMimeType(), "", 1L, 2L);
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
}