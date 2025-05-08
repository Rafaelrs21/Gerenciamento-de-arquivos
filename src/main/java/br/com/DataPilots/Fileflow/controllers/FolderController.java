package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.dtos.CreateFolderRequestDTO;
import br.com.DataPilots.Fileflow.dtos.DefaultResponseDTO;
import br.com.DataPilots.Fileflow.dtos.FolderDTO;
import br.com.DataPilots.Fileflow.entities.Folder;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.FolderAlreadyExistsException;
import br.com.DataPilots.Fileflow.exceptions.InvalidFolderException;
import br.com.DataPilots.Fileflow.services.FileService;
import br.com.DataPilots.Fileflow.services.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/folder")
@RequiredArgsConstructor
public class FolderController {
    private final FolderService folderService;
    private final FileService fileService;

    
    @GetMapping("/list")
    public ResponseEntity<List<FolderDTO>> getListFolders(@AuthenticationPrincipal User user) {
        try {
            List<FolderDTO> listFolder = this.folderService.getFolders(user.getId());
            return ResponseEntity.ok(listFolder);
        } catch (Exception exception) {
            throw new InvalidFolderException();
        }
    }

    @PostMapping
    public ResponseEntity<FolderDTO> createFolder(@AuthenticationPrincipal User user,
                                                           @RequestBody @Valid CreateFolderRequestDTO body) {
        try {
            Folder folder = folderService.create(user.getId(), body.name());
            return ResponseEntity.status(201).body(new FolderDTO(folder.getId(), folder.getName()));
        } catch (Exception exception) {
            throw new FolderAlreadyExistsException();
        }
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<DefaultResponseDTO> deleteFolder(@AuthenticationPrincipal User user,
                                                           @PathVariable Long folderId) {
        try {
            this.folderService.delete(user.getId(), folderId);
            return this.folderDeletedResponse();
        } catch (Exception exception) {
            return this.badRequestResponse(exception.getMessage());
        }
    }

    @GetMapping("/{folderId}/files")
    public List<Map<String, Object>> getFilesByFolder(@AuthenticationPrincipal User user, @PathVariable Long folderId) {
        return fileService.getFilesByFolder(user.getId(), folderId).stream().map(file -> {
            Map<String, Object> serializedFile = file.serialize();
            serializedFile.remove("base64");
            return serializedFile;
        }).toList();
    }

    private ResponseEntity<DefaultResponseDTO> badRequestResponse(String message) {
        var response = new DefaultResponseDTO(message);
        return ResponseEntity.status(400).body(response);
    }

    private ResponseEntity<DefaultResponseDTO> folderDeletedResponse() {
        return ResponseEntity.ok(new DefaultResponseDTO("Pasta deletada."));
    }
}
