package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.dtos.CreateFileShareDTO;
import br.com.DataPilots.Fileflow.dtos.FileShareResponseDTO;
import br.com.DataPilots.Fileflow.entities.FileShare;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.services.FileShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/share")
@RequiredArgsConstructor
public class FileShareController {
    private final FileShareService fileShareService;

    @PostMapping
    public ResponseEntity<FileShareResponseDTO> createShare(
            @AuthenticationPrincipal User user,
            @RequestBody CreateFileShareDTO dto) {
        FileShare share = fileShareService.createShare(dto, user.getId());
        return ResponseEntity.ok(FileShareResponseDTO.fromFileShare(share));
    }

    @GetMapping("/{shareId}")
    public ResponseEntity<FileShareResponseDTO> getFileShareById(
            @AuthenticationPrincipal User user,
            @PathVariable Long shareId) {
        return fileShareService.findByIdAndUser(shareId, user.getId())
                .map(FileShareResponseDTO::fromFileShare)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/public/{token}")
    public ResponseEntity<FileShareResponseDTO> getFileShareByPublicToken(@PathVariable String token) {
        return fileShareService.findByPublicToken(token)
                .map(FileShareResponseDTO::fromFileShare)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/shared-with-me")
    public ResponseEntity<List<FileShareResponseDTO>> getSharesByUser(@AuthenticationPrincipal User user) {
        List<FileShare> shares = fileShareService.findSharesByUserId(user.getId());
        List<FileShareResponseDTO> response = shares.stream()
            .map(FileShareResponseDTO::fromFileShare)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-shares")
    public ResponseEntity<List<FileShareResponseDTO>> getSharesCreatedByUser(@AuthenticationPrincipal User user) {
        List<FileShare> shares = fileShareService.findSharesCreatedByUser(user.getId());
        List<FileShareResponseDTO> response = shares.stream()
            .map(FileShareResponseDTO::fromFileShare)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{shareId}")
    public ResponseEntity<Void> deleteShare(
            @AuthenticationPrincipal User user,
            @PathVariable Long shareId) {
        fileShareService.deleteShare(shareId, user.getId());
        return ResponseEntity.ok().build();
    }
}
