package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.Folder;
import br.com.DataPilots.Fileflow.exceptions.FolderAlreadyExistsException;
import br.com.DataPilots.Fileflow.exceptions.FolderNotFoundException;
import br.com.DataPilots.Fileflow.exceptions.InvalidFolderException;
import br.com.DataPilots.Fileflow.exceptions.InvalidFolderPermissionException;
import br.com.DataPilots.Fileflow.repositories.FolderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FolderServiceTest {
    private FolderRepository repository;
    private FolderService folderService;

    @BeforeEach
    public void setup() {
        repository = Mockito.mock(FolderRepository.class);

        folderService = new FolderService(repository);
    }

    @Test
    public void create() {
        Long userId = 1L;
        String folderName = "teste";

        folderService.create(userId, folderName);
        Mockito.verify(repository, Mockito.times(1)).save(Mockito.any(Folder.class));
    }

    @Test
    public void create_whenFolderNameIsEmpty() {
        Long userId = 1L;
        String folderName = "";

        RuntimeException exception = assertThrows(InvalidFolderException.class, () -> folderService.create(userId, folderName));
        assertEquals("A pasta é inválida.", exception.getMessage());
    }

    @Test
    public void create_whenFolderNameInUse() {
        Long userId = 1L;
        String folderName = "teste";

        Folder folder = Mockito.mock(Folder.class);
        List<Folder> folders = new ArrayList<>();
        folders.add(folder);

        Mockito.when(folder.getName()).thenReturn(folderName);
        Mockito.when(repository.findFoldersByUserId(userId)).thenReturn(folders);

        RuntimeException exception = assertThrows(FolderAlreadyExistsException.class, () -> folderService.create(userId, folderName));
        assertEquals("A pasta já existe.", exception.getMessage());
    }

    @Test
    public void delete() {
        Long userId = 1L;
        Long folderId = 2L;

        Folder folder = Mockito.mock(Folder.class);
        Mockito.when(folder.getUserId()).thenReturn(userId);
        Mockito.when(repository.findById(folderId)).thenReturn(Optional.of(folder));

        folderService.delete(userId, folderId);
        Mockito.verify(repository, Mockito.times(1)).delete(folder);
    }

    @Test
    public void delete_whenFolderNotFound() {
        Long userId = 1L;
        Long folderId = 2L;

        Folder folder = Mockito.mock(Folder.class);
        Mockito.when(folder.getUserId()).thenReturn(userId);
        Mockito.when(repository.findById(folderId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(FolderNotFoundException.class, () -> folderService.delete(userId, folderId));
        assertEquals("A pasta não foi encontrada.", exception.getMessage());
    }

    @Test
    public void delete_whenUserIsNotFolderOwner() {
        Long userId = 1L;
        Long folderId = 2L;

        Folder folder = Mockito.mock(Folder.class);
        Mockito.when(folder.getUserId()).thenReturn(3L);
        Mockito.when(repository.findById(folderId)).thenReturn(Optional.of(folder));

        RuntimeException exception = assertThrows(InvalidFolderPermissionException.class, () -> folderService.delete(userId, folderId));
        assertEquals("Você não tem permissão excluir essa pasta.", exception.getMessage());
    }
}
