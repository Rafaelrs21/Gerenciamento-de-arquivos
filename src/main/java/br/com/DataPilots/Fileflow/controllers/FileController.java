package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.FileVersion;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.services.FileService;
import br.com.DataPilots.Fileflow.dtos.DefaultResponseDTO;
import br.com.DataPilots.Fileflow.exceptions.InvalidFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
                request.getFolderId() != null ? request.getFolderId() : 0
            );
            return this.fileCreatedResponse();
        } catch (InvalidFileException exception) {
            return this.badRequestResponse(exception.getMessage());
        }
    }

    @GetMapping("/download/{folderId}/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@AuthenticationPrincipal User user,
                                               @PathVariable String folderId,
                                               @PathVariable String fileName) {
        try {
            Long folder = Objects.equals(folderId, "null") ? 0 : Long.parseLong(folderId);

            String base64File = fileService.downloadFile(fileName, user.getId(), folder);
            byte[] fileBytes = Base64.getDecoder().decode(base64File);

            String contentType = Files.probeContentType(Path.of(fileName));
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDisposition(ContentDisposition.inline().filename(fileName).build());

            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);

        } catch (InvalidFileException | IllegalArgumentException | IOException e) {
            throw new InvalidFileException();
        }
    }


    @GetMapping
    public List<Map<String, Object>> getFilesByUser(@AuthenticationPrincipal User user) {
        return fileService.getFilesByUser(user.getId()).stream().map(File::serialize).toList();
    }

    @DeleteMapping("/{folderId}/{fileName}")
    public ResponseEntity<DefaultResponseDTO> deleteFile(@AuthenticationPrincipal User user,
                                                         @PathVariable Long folderId,
                                                         @PathVariable String fileName) {
        Optional<File> fileOptional = fileService.findByNameAndUserIdAndFolderId(fileName, user.getId(), folderId);

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

    @PutMapping("/{fileId}")
    public ResponseEntity<Map<String, Object>> updateFile(@AuthenticationPrincipal User user,
                                                          @PathVariable Long fileId,
                                                          @RequestBody File fileUpdate) {
        try {
            fileUpdate.setId(fileId);
            FileVersion version = fileService.updateFile(fileUpdate, user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Arquivo atualizado com versionamento automático.");
            response.put("versionId", version.getId());
            response.put("versionNumber", version.getVersionNumber());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
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