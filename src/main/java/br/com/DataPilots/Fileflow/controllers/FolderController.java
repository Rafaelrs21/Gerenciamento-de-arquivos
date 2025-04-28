package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.dtos.CreateFolderRequestDTO;
import br.com.DataPilots.Fileflow.dtos.DefaultResponseDTO;
import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.User;
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

    @PostMapping
    public ResponseEntity<DefaultResponseDTO> createFolder(@AuthenticationPrincipal User user,
                                                           @RequestBody @Valid CreateFolderRequestDTO body) {
        try {
            this.folderService.create(user.getId(), body.name());
            return this.folderCreatedResponse();
        } catch (Exception exception) {
            return this.badRequestResponse(exception.getMessage());
        }
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<DefaultResponseDTO> deleteFolder(@AuthenticationPrincipal User user,
                                                           @PathVariable Long folderId) {
        try {
            this.folderService.delete(user.getId(), folderId);
            return this.folderDeletedResponse();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return this.badRequestResponse(exception.getMessage());
        }
    }

    @GetMapping("/{folderId}/files")
    public List<Map<String, Object>> getFilesByFolder(@AuthenticationPrincipal User user, @PathVariable Long folderId) {
        return fileService.getFilesByFolder(user.getId(), folderId).stream().map(File::serialize).toList();
    }

    private ResponseEntity<DefaultResponseDTO> badRequestResponse(String message) {
        var response = new DefaultResponseDTO(message);
        return ResponseEntity.status(400).body(response);
    }

    private ResponseEntity<DefaultResponseDTO> folderCreatedResponse() {
        var response = new DefaultResponseDTO("Pasta criada.");
        return ResponseEntity.status(201).body(response);
    }

    private ResponseEntity<DefaultResponseDTO> folderDeletedResponse() {
        return ResponseEntity.ok(new DefaultResponseDTO("Pasta deletada."));
    }
}
