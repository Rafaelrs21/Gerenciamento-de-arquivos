package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.dtos.CreateFileShareDTO;
import br.com.DataPilots.Fileflow.entities.FileShare;
import br.com.DataPilots.Fileflow.services.FileShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/share")
@RequiredArgsConstructor
public class FileShareController {
    private final FileShareService fileShareService;

    @PostMapping
    public ResponseEntity<FileShare> createShare(@RequestBody CreateFileShareDTO dto) {
        return ResponseEntity.ok(fileShareService.createShare(dto));
    }

    @GetMapping("/{shareId}")
    public ResponseEntity<FileShare> getFileShareById(
            @PathVariable Long shareId,
            @RequestParam(required = false) Long userId) {
        
        if (userId != null) {
            return ResponseEntity.ok(fileShareService.findByIdAndUser(shareId, userId));
        } else {
            FileShare share = fileShareService.findById(shareId);
            if (!share.isPublic()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(share);
        }
    }

    @GetMapping("/public/{token}")
    public ResponseEntity<FileShare> getFileShareByPublicToken(@PathVariable String token) {
        return ResponseEntity.ok(fileShareService.findByPublicToken(token));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FileShare>> getSharesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(fileShareService.findSharesByUserId(userId));
    }

    @GetMapping("/created/{userId}")
    public ResponseEntity<List<FileShare>> getSharesCreatedByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(fileShareService.findSharesCreatedByUser(userId));
    }

    @DeleteMapping("/{shareId}")
    public ResponseEntity<Void> deleteShare(@PathVariable Long shareId) {
        fileShareService.deleteShare(shareId);
        return ResponseEntity.ok().build();
    }
}
