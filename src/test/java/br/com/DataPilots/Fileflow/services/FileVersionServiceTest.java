package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.FileVersion;
import br.com.DataPilots.Fileflow.repositories.FileVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileVersionServiceTest {
    private FileVersionRepository repository;

    private FileVersionService fileVersionService;

    @BeforeEach
    public void setUp() {
        repository = Mockito.mock(FileVersionRepository.class);
        fileVersionService = new FileVersionService(repository);
    }

    @Test
    public void listVersionsByFileId() {
        Long fileId = 1L;

        List<FileVersion> versions = new ArrayList<>();
        versions.add(Mockito.mock(FileVersion.class));

        Mockito.when(repository.findByFileIdOrderByVersionNumberDesc(fileId)).thenReturn(versions);

        List<FileVersion> result = fileVersionService.listVersionsByFileId(fileId);

        assertEquals(versions, result);
    }

    @Test
    public void getVersionById() {
        Long versionId = 1L;

        FileVersion fileVersion = Mockito.mock(FileVersion.class);

        Mockito.when(repository.findById(versionId)).thenReturn(Optional.of(fileVersion));

        var result = fileVersionService.getVersionById(versionId);

        assertEquals(fileVersion, result);
    }

    @Test
    public void getVersionById_whenNotFound() {
        Long versionId = 1L;

        Mockito.when(repository.findById(versionId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> fileVersionService.getVersionById(versionId));

        assertEquals("Versão não encontrada", exception.getMessage());
    }

    @Test
    public void getLatestVersion() {
        File file = Mockito.mock(File.class);
        FileVersion fileVersion = Mockito.mock(FileVersion.class);

        Mockito.when(repository.findTopByFileOrderByVersionNumberDesc(file)).thenReturn(fileVersion);

        var result = fileVersionService.getLatestVersion(file);

        assertEquals(fileVersion, result);
    }
}
