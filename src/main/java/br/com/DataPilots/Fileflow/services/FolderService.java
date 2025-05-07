package br.com.DataPilots.Fileflow.services;


import br.com.DataPilots.Fileflow.dtos.FolderDTO;
import br.com.DataPilots.Fileflow.entities.Folder;
import br.com.DataPilots.Fileflow.exceptions.FolderAlreadyExistsException;
import br.com.DataPilots.Fileflow.exceptions.FolderNotFoundException;
import br.com.DataPilots.Fileflow.exceptions.InvalidFolderException;
import br.com.DataPilots.Fileflow.exceptions.InvalidFolderPermissionException;
import br.com.DataPilots.Fileflow.repositories.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository repository;

    public List<FolderDTO> getFolders(Long userId) {
        List<Folder> folders = repository.findFoldersByUserId(userId);
        return folders.stream().map(folder -> new FolderDTO(folder.getId(), folder.getName())).toList();
    }

    public Folder create(Long userId, String folderName) {
        this.checkParams(folderName, userId);

        Folder folder = new Folder(null, userId, folderName);
        return this.repository.save(folder);
    }

    public void delete(Long userId, Long folderId) {
        Folder folder = this.repository.findById(folderId).orElse(null);
        if (folder == null) {
            throw new FolderNotFoundException();
        }

        if (!folder.getUserId().equals(userId)) {
            throw new InvalidFolderPermissionException();
        }

        this.repository.delete(folder);
    }

    private boolean checkParams(String folderName, Long userId) {
        if (folderName == null || folderName.isEmpty()) {
            throw new InvalidFolderException();
        }

        if (this.isFolderNameInUse(folderName, userId)) {
            throw new FolderAlreadyExistsException();
        }

        return true;
    }

    private boolean isFolderNameInUse(String name, Long userId) {
        List<Folder> folders = this.repository.findFoldersByUserId(userId);
        return folders.stream().anyMatch(folder -> folder.getName().equals(name));
    }
}