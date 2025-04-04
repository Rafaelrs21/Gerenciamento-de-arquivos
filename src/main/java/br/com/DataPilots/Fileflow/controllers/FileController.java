package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.services.FileService;
import br.com.DataPilots.Fileflow.dtos.DefaultResponseDTO;
import br.com.DataPilots.Fileflow.exceptions.InvalidFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<DefaultResponseDTO> createFile(@AuthenticationPrincipal User user,
                                                         @RequestBody File request) {
        try {
            fileService.create(
                request.getName(),
                request.getMimeType(),
                request.getBase64(),
                user.getId(),
                request.getFolderId()
            );
            return this.fileCreatedResponse();
        } catch (InvalidFileException exception) {
            return this.badRequestResponse(exception.getMessage());
        }
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<?> downloadFile(@PathVariable String name) {
        try {
            String base64File = fileService.downloadFile(name);
            return ResponseEntity.ok(Map.of("fileName", name, "base64", base64File));
        } catch (InvalidFileException e) {
            return this.badRequestResponse(e.getMessage());
        }
    }

    @GetMapping("/user")
    public List<Map<String, Object>> getFilesByUser(@AuthenticationPrincipal User user) {
        return fileService.getFilesByUser(user.getId()).stream().map(File::serialize).toList();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<DefaultResponseDTO> deleteFile(@PathVariable String name, @AuthenticationPrincipal User user) {
        Optional<File> fileOptional = fileService.getFileByName(name);

        if (fileOptional.isEmpty()) {
            return this.badRequestResponse("Arquivo não encontrado.");
        }

        File file = fileOptional.get();


        if (!file.getUserId().equals(user.getId())) {
            return this.badRequestResponse("Você não tem permissão para excluir este arquivo.");
        }

        fileService.delete(file);
        return this.fileDeletedResponse();
    }

    private ResponseEntity<DefaultResponseDTO> badRequestResponse(String message) {
        var response = new DefaultResponseDTO(message);
        return ResponseEntity.status(400).body(response);
    }

    private ResponseEntity<DefaultResponseDTO> fileCreatedResponse() {
        var response = new DefaultResponseDTO("Arquivo criado.");
        return ResponseEntity.status(201).body(response);
    }

    private ResponseEntity<DefaultResponseDTO> fileDeletedResponse() {
        return ResponseEntity.ok(new DefaultResponseDTO("Arquivo deletado."));
    }
}